package com.runicrealms.plugin.shop;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.item.shops.RunicItemRunnable;
import com.runicrealms.plugin.item.shops.RunicShopItem;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Child class for RunicShopItem that requires an achievement to purchase / use item
 */
public class RunicAchievementItem extends RunicShopItem {

    private final Achievement requiredAchievement;

    /**
     * @param requiredAchievement the achievement required
     */
    public RunicAchievementItem(Map<String, Integer> requiredItems, ItemStack shopItem,
                                RunicItemRunnable runicItemRunnable, Achievement requiredAchievement) {
        super(requiredItems, shopItem, runicItemRunnable);
        this.requiredAchievement = requiredAchievement;
    }

    public Achievement getRequiredAchievement() {
        return requiredAchievement;
    }
}
