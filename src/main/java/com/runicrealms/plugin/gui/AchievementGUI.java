package com.runicrealms.plugin.gui;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementSet;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import com.runicrealms.plugin.utilities.ChatUtils;
import com.runicrealms.plugin.utilities.ColorUtil;
import com.runicrealms.plugin.utilities.GUIUtil;
import com.runicrealms.plugin.utilities.NumRounder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AchievementGUI implements InventoryHolder {

    private final Inventory inventory;
    private final Player player;

    public AchievementGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 54, ColorUtil.format("&eYour Achievements"));
        this.player = player;
        openMenu();
    }

    /**
     * Calculates the total achievement points of the player
     *
     * @param achievementData their data wrapper object
     * @return points for each achievement
     */
    private static int calculateTotalAchievementPoints(AchievementData achievementData) {
        int totalPoints = 0;
        for (AchievementStatus achievementStatus : achievementData.getAchievementStatusList().values()) {
            if (!achievementStatus.isUnlocked()) continue;
            totalPoints += achievementStatus.getAchievement().getPointValue();
        }
        return totalPoints;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public Player getPlayer() {
        return this.player;
    }

    /**
     * Creates a visual progress bar for progress-based achievements in the ui
     *
     * @param achievementStatus the current status of the achievement progress
     * @return a visual string to demonstrate progress
     */
    private static String buildProgressBar(AchievementStatus achievementStatus) {
        String bar = "❚❚❚❚❚❚❚❚❚❚"; // 10 bars
        double current = achievementStatus.getProgress();
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        int max = progressUnlock.getAmountToUnlock();
        double progress = current / max;
        int progressRounded = (int) NumRounder.round(progress * 100);
        int percent = progressRounded / 10;
        return ChatColor.GREEN + bar.substring(0, percent) + ChatColor.WHITE + bar.substring(percent) +
                " (" + ((int) current) + "/" + (max) + ") " +
                ChatColor.GREEN + ChatColor.BOLD + progressRounded + "% ";
    }

    /**
     * Opens the inventory associated w/ this GUI, ordering perks
     */
    private void openMenu() {
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
        this.inventory.clear();
        GUIUtil.fillInventoryBorders(this.inventory);
        this.inventory.setItem(0, GUIUtil.closeButton());
        this.inventory.setItem(4, achievementInfoItem(achievementData));
        for (Achievement achievement : Achievement.values()) {
            AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(achievement.getId());
            this.inventory.setItem(inventory.firstEmpty(), achievementItem(achievementStatus, achievement));
        }
    }

    /**
     * Creates an ItemStack which contains all visual information for an achievement in the UI
     *
     * @param achievementStatus the current progress of the achievement
     * @param achievement       the enumerated achievement itself
     * @return an ItemStack menu item
     */
    private ItemStack achievementItem(AchievementStatus achievementStatus, Achievement achievement) {
        String displayName = achievement.getName();
        ItemStack menuItem = new ItemStack(achievement.getGuiItem());
        ItemMeta meta = menuItem.getItemMeta();
        if (meta == null) return menuItem;
        meta.setDisplayName(ChatColor.YELLOW + displayName);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "[" + achievement.getPointValue() + "] pts");
        lore.add("");
        lore.addAll(ChatUtils.formattedText("&7" + achievement.getDescription()));
        lore.add("");
        lore.add
                (
                        ChatColor.YELLOW + "Status: " + (achievementStatus.isUnlocked() ?
                                ChatColor.GREEN + "" + ChatColor.BOLD + "COMPLETE" :
                                ChatColor.RED + "" + ChatColor.BOLD + "INCOMPLETE")
                );
        if (achievement.getUnlockMethod() instanceof ProgressUnlock) {
            lore.add(buildProgressBar(achievementStatus));
        }
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "Reward(s):");
        for (Reward reward : achievement.getRewards()) {
            lore.add(ChatColor.GREEN + "- " + reward.getRewardMessage());
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Set: " + ChatColor.DARK_AQUA + achievement.getAchievementSet().getName());
        meta.setLore(lore);
        ((Damageable) meta).setDamage(5);
        meta.setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        menuItem.setItemMeta(meta);
        return menuItem;
    }

    private ItemStack achievementInfoItem(AchievementData achievementData) {
        ItemStack menuItem = new ItemStack(Material.PAPER);
        ItemMeta meta = menuItem.getItemMeta();
        if (meta == null) return menuItem;
        meta.setDisplayName(ChatColor.YELLOW + "Achievement Info");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GOLD + "Achievement Points: " + calculateTotalAchievementPoints(achievementData));
        lore.addAll(ChatUtils.formattedText("&7Achievement points track your total achievement progress!"));
        lore.add("");
        lore.add(ChatColor.DARK_AQUA + "Set Progress:");
        for (AchievementSet achievementSet : AchievementSet.values()) {
            int total = AchievementSet.getTotalAchievementsInSet(achievementSet);
            int progress = 0;
            for (AchievementStatus achievementStatus : achievementData.getAchievementStatusList().values()) {
                if (!achievementStatus.isUnlocked()) continue;
                if (achievementStatus.getAchievement().getAchievementSet() != achievementSet) continue;
                progress += 1;
            }
            lore.add(ChatColor.AQUA + "- [" + ChatColor.WHITE + progress + ChatColor.AQUA + "/" + total + "] " + achievementSet.getName());
        }
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        menuItem.setItemMeta(meta);
        return menuItem;
    }
}