package com.runicrealms.plugin.shop;

import com.runicrealms.plugin.*;
import com.runicrealms.plugin.api.RunicCoreAPI;
import com.runicrealms.plugin.item.shops.RunicItemRunnable;
import com.runicrealms.plugin.item.shops.RunicShopGeneric;
import com.runicrealms.plugin.item.shops.RunicShopItem;
import com.runicrealms.plugin.model.AchievementData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedHashSet;

public class FastTravelShopInitializer {

    public FastTravelShopInitializer() {
        RunicCoreAPI.registerRunicItemShop(fastTravelShop());
    }

    private ItemStack wagonItem(TravelLocation travelLocation, String requiredAchievement) {
        ItemStack wagonItem = new ItemStack(TravelType.WAGON.getMaterial());
        ItemMeta meta = wagonItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName(ChatColor.GREEN + "Fast Travel: " + travelLocation.getDisplay());
        meta.setLore(Arrays.asList
                (
                        "",
                        ChatColor.GRAY + "Required Achievement: ",
                        ChatColor.DARK_AQUA + "- [" + ChatColor.AQUA + requiredAchievement + ChatColor.DARK_AQUA + "]",
                        "",
                        ChatColor.GRAY + "Fast travel to this destination!"
                ));
        wagonItem.setItemMeta(meta);
        return wagonItem;
    }

    public RunicShopGeneric fastTravelShop() {
        LinkedHashSet<RunicShopItem> shopItems = new LinkedHashSet<>();
        shopItems.add
                (
                        new RunicShopItem(0, "Coin",
                                wagonItem(TravelLocation.AZANA, "None"),
                                "FREE",
                                runWagonBuy(TravelLocation.AZANA, "")
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.KOLDORE, Achievement.DISCOVER_KOLDORE.getName()),
                                runWagonBuy(TravelLocation.KOLDORE, Achievement.DISCOVER_KOLDORE.getId()),
                                Achievement.DISCOVER_KOLDORE
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.WHALETOWN, Achievement.DISCOVER_WHALETOWN.getName()),
                                runWagonBuy(TravelLocation.WHALETOWN, Achievement.DISCOVER_WHALETOWN.getId()),
                                Achievement.DISCOVER_WHALETOWN
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.HILSTEAD, Achievement.DISCOVER_HILSTEAD.getName()),
                                runWagonBuy(TravelLocation.HILSTEAD, Achievement.DISCOVER_HILSTEAD.getId()),
                                Achievement.DISCOVER_HILSTEAD
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.WINTERVALE, Achievement.DISCOVER_WINTERVALE.getName()),
                                runWagonBuy(TravelLocation.WINTERVALE, Achievement.DISCOVER_WINTERVALE.getId()),
                                Achievement.DISCOVER_WINTERVALE
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.DEAD_MANS_REST, Achievement.DISCOVER_DEAD_MANS_REST.getName()),
                                runWagonBuy(TravelLocation.DEAD_MANS_REST, Achievement.DISCOVER_DEAD_MANS_REST.getId()),
                                Achievement.DISCOVER_DEAD_MANS_REST
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.ISFODAR, Achievement.DISCOVER_ISFODAR.getName()),
                                runWagonBuy(TravelLocation.ISFODAR, Achievement.DISCOVER_ISFODAR.getId()),
                                Achievement.DISCOVER_ISFODAR
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.TIRNEAS, Achievement.DISCOVER_TIRENEAS.getName()),
                                runWagonBuy(TravelLocation.TIRNEAS, Achievement.DISCOVER_TIRENEAS.getId()),
                                Achievement.DISCOVER_TIRENEAS
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.ZENYTH, Achievement.DISCOVER_ZENYTH.getName()),
                                runWagonBuy(TravelLocation.ZENYTH, Achievement.DISCOVER_ZENYTH.getId()),
                                Achievement.DISCOVER_ZENYTH
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.NAHEEN, Achievement.DISCOVER_NAHEEN.getName()),
                                runWagonBuy(TravelLocation.NAHEEN, Achievement.DISCOVER_NAHEEN.getId()),
                                Achievement.DISCOVER_NAHEEN
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.NAZMORA, Achievement.DISCOVER_NAZMORA.getName()),
                                runWagonBuy(TravelLocation.NAZMORA, Achievement.DISCOVER_NAZMORA.getId()),
                                Achievement.DISCOVER_NAZMORA
                        )
                );
        shopItems.add
                (
                        new RunicAchievementItem(0, "Coin",
                                wagonItem(TravelLocation.FROSTS_END, Achievement.DISCOVER_FROSTS_END.getName()),
                                runWagonBuy(TravelLocation.FROSTS_END, Achievement.DISCOVER_FROSTS_END.getId()),
                                Achievement.DISCOVER_FROSTS_END
                        )
                );
        shopItems.forEach(runicShopItem -> runicShopItem.setRemovePayment(false));
        return new RunicShopGeneric(45, ChatColor.YELLOW + "Wagonmaster", Arrays.asList(245, 246, 249, 256, 262, 267, 333, 272, 334, 285, 315, 337), shopItems);
    }

    /**
     * Allows a player to fast travel if they are not already there and have met the level requirement
     *
     * @param travelLocation the travel location, which is slightly different from city location, and is centered around wagon masters
     * @param achievementId  of achievement to use the travel point
     * @return a runnable
     */
    private RunicItemRunnable runWagonBuy(TravelLocation travelLocation, String achievementId) {
        return player -> {
            AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
            AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievementId);
            if (!achievementId.equals("") && !achievementStatus.isUnlocked()) {
                player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5f, 1.0f);
                player.sendMessage(ChatColor.RED + "You must visit this location to unlock this fast travel!");
            } else {
                TravelLocation.fastTravelTask(player, TravelType.WAGON, travelLocation);
            }
        };
    }
}
