package com.runicrealms.plugin.achievements.ui;

import com.runicrealms.plugin.achievements.Achievement;
import com.runicrealms.plugin.achievements.AchievementSet;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.api.Reward;
import com.runicrealms.plugin.achievements.model.AchievementData;
import com.runicrealms.plugin.achievements.reward.TitleReward;
import com.runicrealms.plugin.achievements.unlock.ProgressUnlock;
import com.runicrealms.plugin.achievements.util.AchievementUtil;
import com.runicrealms.plugin.common.util.ChatUtils;
import com.runicrealms.plugin.common.util.ColorUtil;
import com.runicrealms.plugin.common.util.GUIUtil;
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

public class AchievementUI implements InventoryHolder {
    public static final int INVENTORY_SIZE = 28;
    public static final ItemStack REMOVE_TITLE_ITEM;
    private static final int MAX_PAGES = 2;

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

    private final Inventory inventory;
    private final Player player;
    private int currentPage;

    public AchievementUI(Player player) {
        this.inventory = Bukkit.createInventory(this, 54, ColorUtil.format("&eYour Achievements"));
        this.player = player;
        this.currentPage = 1;
        updateMenu();
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

    private ItemStack achievementInfoItem(AchievementData achievementData) {
        ItemStack menuItem = new ItemStack(Material.PAPER);
        ItemMeta meta = menuItem.getItemMeta();
        if (meta == null) return menuItem;
        meta.setDisplayName(ChatColor.YELLOW + "Achievement Info");
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.GOLD + "Achievement Points: " + AchievementUtil.calculateTotalAchievementPoints(achievementData));
        lore.addAll(ChatUtils.formattedText("&7Achievement points track your total achievement progress!"));
        lore.add("");
        lore.add(ChatColor.DARK_AQUA + "Set Progress:");
        for (AchievementSet achievementSet : AchievementSet.values()) {
            int total = AchievementSet.getTotalAchievementsInSet(achievementSet, true);
            int progress = 0;
            for (AchievementStatus achievementStatus : achievementData.getAchievementStatusMap().values()) {
                if (!achievementStatus.isUnlocked()) continue;
                if (achievementStatus.getAchievement().getAchievementSet() != achievementSet)
                    continue;
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

    private void fillAchievementsForPage(AchievementData achievementData) {
        int location = (currentPage - 1) * (INVENTORY_SIZE + 1); // holds our place in the list of pages
        Achievement[] achievements = Achievement.values();

        for (int i = location; i < achievements.length; i++) {
            Achievement achievement = achievements[i];
            AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
            if (this.getInventory().firstEmpty() == -1) return; // inventory is filled
            if (achievementStatus == null || !achievement.shouldDisplay())
                continue; // Error loading achievements, data is likely being unloaded right now
            this.inventory.setItem(inventory.firstEmpty(), achievementItem(achievementStatus, achievement));
        }
    }

    public int getCurrentPage() {
        return currentPage;
    }

    private void setCurrentPage(int page) {
        this.currentPage = page;
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
     * Sets the internal current page to 1, updates the inventory, and opens it
     */
    public void openFirstPage() {
        this.setCurrentPage(1);
        this.updateMenu();
        player.openInventory(inventory);
    }

    /**
     * Sets the internal current page to 1 + current, updates the inventory, and opens it
     */
    public void openNextPage() {
        if ((currentPage + 1) > MAX_PAGES) return;
        this.setCurrentPage(currentPage + 1);
        this.updateMenu();
        player.openInventory(inventory);
    }

    /**
     * Opens the inventory associated w/ this GUI, ordering perks
     */
    public void updateMenu() {
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(player.getUniqueId());
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
}