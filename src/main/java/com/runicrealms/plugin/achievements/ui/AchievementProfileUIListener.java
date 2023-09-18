package com.runicrealms.plugin.achievements.ui;

import com.runicrealms.plugin.player.ui.ProfileUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

/**
 * Listens for when player clicks their crafting slots inventory menu to open achievement GUI
 */
public class AchievementProfileUIListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getView().getTopInventory().getHolder() instanceof ProfileUI)) return;
        if (event.getClickedInventory().getType() == InventoryType.PLAYER) {
            event.setCancelled(true);
            return;
        }
        ProfileUI profileUI = (ProfileUI) event.getClickedInventory().getHolder();
        if (!event.getWhoClicked().equals(profileUI.getPlayer())) {
            event.setCancelled(true);
            event.getWhoClicked().closeInventory();
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (event.getCurrentItem() == null) return;
        if (profileUI.getInventory().getItem(event.getRawSlot()) == null) return;

        if (event.getSlot() == 20) {
            event.setCancelled(true);
            player.openInventory(new AchievementUI(player).getInventory());
        }
    }
}
