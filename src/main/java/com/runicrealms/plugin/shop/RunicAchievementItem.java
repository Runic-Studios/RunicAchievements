package com.runicrealms.plugin.shop;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.item.shops.RunicItemRunnable;
import com.runicrealms.plugin.item.shops.RunicShopItem;
import org.bukkit.inventory.ItemStack;

/**
 * Child class for RunicShopItem that requires an achievement to purchase / use item
 */
public class RunicAchievementItem extends RunicShopItem {

    private final Achievement requiredAchievement;

    /**
     * @param price               of the item
     * @param currencyTemplateID  the runic item id of the cost
     * @param shopItem            an itemStack used in the display
     * @param runicItemRunnable   the runnable to execute on purchase
     * @param requiredAchievement the achievement required
     */
    public RunicAchievementItem(int price, String currencyTemplateID, ItemStack shopItem,
                                RunicItemRunnable runicItemRunnable, Achievement requiredAchievement) {
        super(price, currencyTemplateID, shopItem, "FREE", runicItemRunnable);
        this.requiredAchievement = requiredAchievement;
    }

    public Achievement getRequiredAchievement() {
        return requiredAchievement;
    }
}
