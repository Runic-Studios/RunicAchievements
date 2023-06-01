package com.runicrealms.plugin.ui;

import co.aikar.taskchain.TaskChain;
import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.common.util.GUIUtil;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.model.AchievementManager;
import com.runicrealms.plugin.model.TitleData;
import com.runicrealms.plugin.rdb.RunicDatabase;
import com.runicrealms.plugin.reward.TitleReward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import redis.clients.jedis.Jedis;

public class AchievementUIListener implements Listener {

    /**
     * Attempts to set the achievement title for the given player, based on the material of the icon (must be unique)
     * Fails if the player has not unlocked the title
     *
     * @param player      to check
     * @param displayName of the achievement icon
     */
    private void attemptToSetTitle(Player player, String displayName) {
        Achievement achievement = Achievement.getFromDisplayName(displayName);
        if (achievement == null) return;
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(player.getUniqueId());
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus == null) return;
        if (!achievementStatus.isUnlocked()) return;
        player.closeInventory();

        TitleReward titleReward = null;
        for (Reward reward : achievement.getRewards()) {
            if (!(reward instanceof TitleReward)) continue;
            titleReward = (TitleReward) reward;
            break;
        }
        if (titleReward == null) return;

        // Write Title Data Async
        String completeMessage = ChatColor.DARK_AQUA + "You have enabled the title: " + ChatColor.AQUA + titleReward.getTitle() + ChatColor.DARK_AQUA + "!";
        TaskChain<?> chain = RunicAchievements.newChain();
        TitleReward finalTitleReward = titleReward;
        chain
                .asyncFirst(() -> {
                    try (Jedis jedis = RunicDatabase.getAPI().getRedisAPI().getNewJedisResource()) {
                        TitleData titleData = RunicCore.getTitleAPI().loadTitleData(player.getUniqueId(), jedis);
                        if (finalTitleReward.isSuffix()) {
                            titleData.setSuffix(finalTitleReward.getTitle());
                        } else {
                            titleData.setPrefix(finalTitleReward.getTitle());
                        }
                        titleData.writeToJedis(player.getUniqueId(), jedis);
                        return titleData;
                    }
                })
                .abortIfNull(AchievementManager.CONSOLE_LOG, null, "RunicAchievements failed to write title!")
                .syncLast(data -> player.sendMessage(completeMessage))
                .execute();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getView().getTopInventory().getHolder() instanceof AchievementUI)) return;
        // prevent clicking items in player inventory
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }
        AchievementUI achievementUI = (AchievementUI) event.getClickedInventory().getHolder();
        if (achievementUI == null) return;

        // Ensure correct player is clicking
        if (!event.getWhoClicked().equals(achievementUI.getPlayer())) {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (achievementUI.getInventory().getItem(event.getRawSlot()) == null) return;
        ItemStack item = event.getCurrentItem();
        Material material = item.getType();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
        event.setCancelled(true);
        if (material == GUIUtil.CLOSE_BUTTON.getType())
            event.getWhoClicked().closeInventory();
        else if (material == GUIUtil.BACK_BUTTON.getType())
            achievementUI.openFirstPage();
        else if (material == AchievementUI.REMOVE_TITLE_ITEM.getType()) {
            player.closeInventory();
            RunicCore.getTitleAPI().removePrefixesAndSuffixes(player, () -> {
            });
        } else if (material == GUIUtil.FORWARD_BUTTON.getType())
            achievementUI.openNextPage();
        else if (material != GUIUtil.BORDER_ITEM.getType()) {
            attemptToSetTitle(player, ChatColor.stripColor(item.getItemMeta().getDisplayName()));
        }
    }

}
