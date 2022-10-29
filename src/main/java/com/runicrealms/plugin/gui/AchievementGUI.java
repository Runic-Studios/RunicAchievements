package com.runicrealms.plugin.gui;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.unlocks.ProgressUnlock;
import com.runicrealms.plugin.utilities.ChatUtils;
import com.runicrealms.plugin.utilities.ColorUtil;
import com.runicrealms.plugin.utilities.GUIUtil;
import com.runicrealms.plugin.utilities.NumRounder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
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

    private static int calculateTotalAchievementPoints(AchievementData achievementData) {
        return 0;
//        int totalLevel = 0;
//        for (GatheringSkill gatheringSkill : GatheringSkill.values()) {
//            totalLevel += gatheringData.getGatheringLevel(gatheringSkill);
//        }
//        return totalLevel;
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
     * @param achievementStatus
     * @return
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
                " (" + (current) + "/" + (max) + ") " +
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
        // todo: player head with total achievement points?
//        this.inventory.setItem(4, GUIUtil.dispItem(
//                Material.PAPER,
//                ChatColor.YELLOW + "Skills Info",
//                new String[]{
//                        "",
//                        ChatColor.YELLOW + "Cooking " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.COOKING),
//                        ChatColor.YELLOW + "Farming " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.FARMING),
//                        ChatColor.YELLOW + "Fishing " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.FISHING),
//                        ChatColor.YELLOW + "Harvesting " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.HARVESTING),
//                        ChatColor.YELLOW + "Mining " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.MINING),
//                        ChatColor.YELLOW + "Woodcutting " + ChatColor.GRAY + gatheringData.getGatheringLevel(GatheringSkill.WOODCUTTING),
//                        "",
//                        ChatColor.GRAY + "Total Level " + calculateTotalLevel(gatheringData)
//                }
//        ));
        for (Achievement achievement : Achievement.values()) {
            AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(achievement.getId());
            this.inventory.setItem(inventory.firstEmpty(), achievementItem(achievementStatus, achievement));
        }
    }

    /**
     * @param achievementStatus
     * @param achievement
     * @return
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
                                ChatColor.GREEN + "" + ChatColor.BOLD + "UNLOCKED" :
                                ChatColor.RED + "" + ChatColor.BOLD + "LOCKED")
                );
        lore.add("");
        lore.add
                (
                        ChatColor.YELLOW + "Set: " + ChatColor.DARK_AQUA + "" +
                                ChatColor.BOLD + achievement.getAchievementSet().getName()
                );
        if (achievement.getUnlockMethod() instanceof ProgressUnlock) {
            lore.add(buildProgressBar(achievementStatus));
        }
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        menuItem.setItemMeta(meta);
        return menuItem;
    }
}