package com.runicrealms.plugin.gui;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.model.TitleData;
import com.runicrealms.plugin.reward.TitleReward;
import com.runicrealms.plugin.utilities.GUIUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class AchievementGUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getClickedInventory() == null) return;
        if (!(e.getView().getTopInventory().getHolder() instanceof AchievementGUI)) return;
        // prevent clicking items in player inventory
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            e.setCancelled(true);
            return;
        }
        AchievementGUI achievementGUI = (AchievementGUI) e.getClickedInventory().getHolder();
        // insurance
        if (!e.getWhoClicked().equals(achievementGUI.getPlayer())) {
            e.setCancelled(true);
            e.getWhoClicked().closeInventory();
            return;
        }
        Player player = (Player) e.getWhoClicked();
        if (e.getCurrentItem() == null) return;
        if (achievementGUI.getInventory().getItem(e.getRawSlot()) == null) return;
        ItemStack item = e.getCurrentItem();
        Material material = item.getType();
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1.0f);
        e.setCancelled(true);
        if (material == GUIUtil.closeButton().getType())
            e.getWhoClicked().closeInventory();
        else if (material == AchievementGUI.REMOVE_TITLE_ITEM.getType()) {
            removeTitle(player);
        } else if (material != GUIUtil.borderItem().getType()) {
            attemptToSetTitle(player, material);
        }
    }

    /**
     * Attempts to set the achievement title for the given player, based on the material of the icon (must be unique)
     * Fails if the player has not unlocked the title
     *
     * @param player   to check
     * @param material of the achievement icon
     */
    private void attemptToSetTitle(Player player, Material material) {
        Achievement achievement = Achievement.getFromMaterial(material);
        if (achievement == null) return;
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus == null) return;
        if (!achievementStatus.isUnlocked()) return;
        player.closeInventory();
        for (Reward reward : achievement.getRewards()) {
            if (!(reward instanceof TitleReward)) continue;
            TitleReward titleReward = (TitleReward) reward;
            TitleData titleData = RunicCore.getTitleManager().loadTitleData(player.getUniqueId());
            if (titleReward.isSuffix()) {
                titleData.setSuffix(titleReward.getTitle());
            } else {
                titleData.setPrefix(titleReward.getTitle());
            }
            player.sendMessage(ChatColor.DARK_AQUA + "You have enabled the title: " + ChatColor.AQUA + titleReward.getTitle() + ChatColor.DARK_AQUA + "!");
            return;
        }
    }

    private void removeTitle(Player player) {
        TitleData titleData = RunicCore.getTitleManager().loadTitleData(player.getUniqueId());
        titleData.setPrefix("");
        titleData.setSuffix("");
        player.closeInventory();
    }
}
