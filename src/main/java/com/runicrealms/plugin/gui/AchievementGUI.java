package com.runicrealms.plugin.gui;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementSet;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.reward.TitleReward;
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
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AchievementGUI implements InventoryHolder {
    public static final int INVENTORY_SIZE = 28;
    private static final int MAX_PAGES = 2;

    private final Inventory inventory;
    private final Player player;
    private int currentPage;

    public AchievementGUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 54, ColorUtil.format("&eYour Achievements"));
        this.player = player;
        this.currentPage = 1;
        updateMenu();
    }

    /**
     * Calculates the total achievement points of the player
     *
     * @param achievementData their data wrapper object
     * @return points for each achievement
     */
    private static int calculateTotalAchievementPoints(AchievementData achievementData) {
        int totalPoints = 0;
        for (AchievementStatus achievementStatus : achievementData.getAchievementStatusMap().values()) {
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

    public int getCurrentPage() {
        return currentPage;
    }

    private void setCurrentPage(int page) {
        this.currentPage = page;
    }

    /**
     *
     */
    public void openFirstPage() {
        this.setCurrentPage(1);
        this.updateMenu();
        player.openInventory(inventory);
    }

    /**
     *
     */
    public void openNextPage() {
        if ((currentPage + 1) > MAX_PAGES) return;
        this.setCurrentPage(currentPage + 1);
        this.updateMenu();
        player.openInventory(inventory);
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
        if (progress > max) {
            progress = max;
        }
        int progressRounded = (int) NumRounder.round(progress * 100);
        int percent = progressRounded / 10;
        if (percent > bar.length())
            percent = bar.length();
        return ChatColor.GREEN + bar.substring(0, percent) + ChatColor.WHITE + bar.substring(percent) +
                " (" + ((int) current) + "/" + (max) + ") " +
                ChatColor.GREEN + ChatColor.BOLD + progressRounded + "% ";
    }

    public static final ItemStack REMOVE_TITLE_ITEM;

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
        if (achievementStatus.isUnlocked()) {
            if (achievement.getRewards().stream().anyMatch(r -> r instanceof TitleReward)) {
                TitleReward titleReward = (TitleReward) achievement.getRewards().stream().filter(r -> r instanceof TitleReward).findFirst().get();
                lore.add("");
                lore.add(ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK " + ChatColor.DARK_AQUA + "to enable title: ");
                lore.add(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + titleReward.getTitle() + ChatColor.DARK_AQUA + "]");
            }
        }
        meta.setLore(lore);
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
            for (AchievementStatus achievementStatus : achievementData.getAchievementStatusMap().values()) {
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

    static {
        REMOVE_TITLE_ITEM = new ItemStack(Material.MILK_BUCKET);
        ItemMeta meta = REMOVE_TITLE_ITEM.getItemMeta();
        assert meta != null;
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.addAll(ChatUtils.formattedText(ChatColor.GRAY + "Clear your current achievement title!"));
        meta.setDisplayName(ChatColor.RED + "Clear Title");
        meta.setLore(lore);
        REMOVE_TITLE_ITEM.setItemMeta(meta);
    }

    /**
     * Opens the inventory associated w/ this GUI, ordering perks
     */
    public void updateMenu() {
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
        this.inventory.clear();
        GUIUtil.fillInventoryBorders(this.inventory);
        if (currentPage == 1)
            this.inventory.setItem(0, GUIUtil.CLOSE_BUTTON);
        else
            this.inventory.setItem(0, GUIUtil.BACK_BUTTON);
        this.inventory.setItem(4, achievementInfoItem(achievementData));
        this.inventory.setItem(5, REMOVE_TITLE_ITEM);
        this.inventory.setItem(8, GUIUtil.FORWARD_BUTTON);
        fillAchievementsForPage(achievementData);
    }

    /**
     * @param achievementData
     */
    private void fillAchievementsForPage(AchievementData achievementData) {
        int location = (currentPage - 1) * INVENTORY_SIZE; // holds our place in the list of pages
        Achievement[] achievements = Achievement.values();

        for (int i = location; i < achievements.length; i++) {
            Achievement achievement = achievements[i];
            AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
            if (this.getInventory().firstEmpty() == -1) return; // inventory is filled
            this.inventory.setItem(inventory.firstEmpty(), achievementItem(achievementStatus, achievement));
        }
    }
}