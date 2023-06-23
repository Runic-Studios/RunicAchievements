package com.runicrealms.plugin.shop;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.TravelLocation;
import com.runicrealms.plugin.TravelType;
import com.runicrealms.plugin.item.shops.RunicItemRunnable;
import com.runicrealms.plugin.item.shops.RunicShopGeneric;
import com.runicrealms.plugin.item.shops.RunicShopItem;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FastTravelShopInitializer {

    public FastTravelShopInitializer() {
        RunicCore.getShopAPI().registerRunicItemShop(fastTravelShop());
    }

    public RunicShopGeneric fastTravelShop() {
        ArrayList<RunicShopItem> shopItems = new ArrayList<>();

        shopItems.add
                (
                        new RunicShopItem(5,
                                wagonItem(TravelLocation.AZANA, "None"),
                                runWagonBuy(TravelLocation.AZANA),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, ""))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(5,
                                wagonItem(TravelLocation.KOLDORE, Achievement.DISCOVER_KOLDORE.getName()),
                                runWagonBuy(TravelLocation.KOLDORE),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_KOLDORE.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(5,
                                wagonItem(TravelLocation.WHALETOWN, Achievement.DISCOVER_WHALETOWN.getName()),
                                runWagonBuy(TravelLocation.WHALETOWN),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_WHALETOWN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(5,
                                wagonItem(TravelLocation.HILSTEAD, Achievement.DISCOVER_HILSTEAD.getName()),
                                runWagonBuy(TravelLocation.HILSTEAD),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_HILSTEAD.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(10,
                                wagonItem(TravelLocation.WINTERVALE, Achievement.DISCOVER_WINTERVALE.getName()),
                                runWagonBuy(TravelLocation.WINTERVALE),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_WINTERVALE.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(10,
                                wagonItem(TravelLocation.DEAD_MANS_REST, Achievement.DISCOVER_DEAD_MANS_REST.getName()),
                                runWagonBuy(TravelLocation.DEAD_MANS_REST),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_DEAD_MANS_REST.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(10,
                                wagonItem(TravelLocation.ISFODAR, Achievement.DISCOVER_ISFODAR.getName()),
                                runWagonBuy(TravelLocation.ISFODAR),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_ISFODAR.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(10,
                                wagonItem(TravelLocation.TIRNEAS, Achievement.DISCOVER_TIRENEAS.getName()),
                                runWagonBuy(TravelLocation.TIRNEAS),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_TIRENEAS.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.ZENYTH, Achievement.DISCOVER_ZENYTH.getName()),
                                runWagonBuy(TravelLocation.ZENYTH),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_ZENYTH.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.NAHEEN, Achievement.DISCOVER_NAHEEN.getName()),
                                runWagonBuy(TravelLocation.NAHEEN),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_NAHEEN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.NAZMORA, Achievement.DISCOVER_NAZMORA.getName()),
                                runWagonBuy(TravelLocation.NAZMORA),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_NAZMORA.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.STONEHAVEN, Achievement.DISCOVER_STONEHAVEN.getName()),
                                runWagonBuy(TravelLocation.STONEHAVEN),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_STONEHAVEN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.AZUREMIST, Achievement.DISCOVER_AZUREMIST.getName()),
                                runWagonBuy(TravelLocation.AZUREMIST),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_AZUREMIST.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(15,
                                wagonItem(TravelLocation.FROSTS_END, Achievement.DISCOVER_FROSTS_END.getName()),
                                runWagonBuy(TravelLocation.FROSTS_END),
                                Collections.singletonList(player -> RunicAchievements.getAchievementsAPI().hasAchievement(player, Achievement.DISCOVER_FROSTS_END.getId()))
                        )
                );
        shopItems.forEach(runicShopItem -> runicShopItem.setRemovePayment(true));
        return new RunicShopGeneric(45, ChatColor.YELLOW + "Wagonmaster", Arrays.asList
                (
                        245, 246, 249, 256, 262, 267, 333, 272, 334, 285, 315, 337, 787
                ), shopItems);
    }

    /**
     * Allows a player to fast travel if they are not already there and have met the level requirement
     *
     * @param travelLocation the travel location, which is slightly different from city location, and is centered around wagon masters
     * @return a runnable
     */
    private RunicItemRunnable runWagonBuy(TravelLocation travelLocation) {
        return player -> TravelLocation.fastTravelTask(player, TravelType.WAGON, travelLocation);
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
}
