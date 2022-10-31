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
        else if (material != GUIUtil.borderItem().getType()) {
            Achievement achievement = Achievement.getFromMaterial(e.getCurrentItem().getType());
            if (achievement == null) return;
            AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
            AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(achievement.getId());
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
            }
        }
    }
}
