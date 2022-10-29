package com.runicrealms.plugin.gui;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.utilities.ChatUtils;
import com.runicrealms.plugin.utilities.ColorUtil;
import com.runicrealms.plugin.utilities.GUIUtil;
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

//    private static String[] gatheringSkillDescription(GatheringData gatheringData, GatheringSkill gatheringSkill) {
//        String[] unlockMessageArray = GatheringLevelChangeListener.nextReagentUnlockMessage(gatheringSkill,
//                gatheringData.getGatheringLevel(gatheringSkill), true).toArray(new String[0]);
//        int level = gatheringData.getGatheringLevel(gatheringSkill);
////        boolean isSpecialized = RunicProfessionsAPI.isSpecializedInGatheringSkill(gatheringData, gatheringSkill);
//        boolean isSpecialized = false;
//        boolean professionIsMaxed = (level == 60 && !isSpecialized) || (level == 100 && isSpecialized);
//        String[] descriptionArray = new String[]{
//                buildProgressBar(gatheringData, gatheringSkill),
//                "",
//                ChatColor.GRAY + "Level: " + ChatColor.WHITE + level + (professionIsMaxed ? " (Cap Reached)" : ""),
//                ChatColor.GRAY + "Exp: " + ChatColor.WHITE + gatheringData.getGatheringExp(gatheringSkill),
//                "",
//                ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK " + ChatColor.GRAY + "to view all available reagents",
//                ""
//        };
//        return (String[]) ArrayUtils.addAll(descriptionArray, unlockMessageArray); // append formatted unlock message
//    }

//    private static String buildProgressBar(GatheringData gatheringData, GatheringSkill gatheringSkill) {
//        String bar = "❚❚❚❚❚❚❚❚❚❚"; // 10 bars
//        int currentExp = gatheringData.getGatheringExp(gatheringSkill);
//        int currentLv = gatheringData.getGatheringLevel(gatheringSkill);
//        int totalExpAtLevel = ProfExpUtil.calculateTotalExperience(currentLv);
//        int totalExpToLevel = ProfExpUtil.calculateTotalExperience(currentLv + 1);
//        double progress = (double) (currentExp - totalExpAtLevel) / (totalExpToLevel - totalExpAtLevel); // 60 - 55 = 5 / 75 - 55 = 20, 5 /20
//        int progressRounded = (int) NumRounder.round(progress * 100);
//        int percent = progressRounded / 10;
//        return ChatColor.GREEN + bar.substring(0, percent) + ChatColor.WHITE + bar.substring(percent) +
//                " (" + (currentExp - totalExpAtLevel) + "/" + (totalExpToLevel - totalExpAtLevel) + ") " +
//                ChatColor.GREEN + ChatColor.BOLD + progressRounded + "% ";
//    }

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
     * Opens the inventory associated w/ this GUI, ordering perks
     */
    private void openMenu() {
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
        this.inventory.clear();
        GUIUtil.fillInventoryBorders(this.inventory);
        this.inventory.setItem(0, GUIUtil.closeButton());
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
        List<String> lore = new ArrayList<>(ChatUtils.formattedText("&7" + achievement.getDescription()));
        lore.add("");
        lore.add("Status: " + (achievementStatus.isUnlocked() ? "&a&lUNLOCKED" : "&c&lNOT YET UNLOCKED"));
        // todo: progress bar
        meta.setLore(ChatUtils.formattedText("&7" + achievement.getDescription()));
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        menuItem.setItemMeta(meta);
        return menuItem;
    }
}