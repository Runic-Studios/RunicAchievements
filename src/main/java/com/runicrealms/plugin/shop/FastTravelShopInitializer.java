package com.runicrealms.plugin.shop;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.TravelLocation;
import com.runicrealms.plugin.TravelType;
import com.runicrealms.plugin.item.shops.RunicItemRunnable;
import com.runicrealms.plugin.item.shops.RunicShopGeneric;
import com.runicrealms.plugin.item.shops.RunicShopItem;
import com.runicrealms.plugin.model.AchievementData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class FastTravelShopInitializer {

    public FastTravelShopInitializer() {
        RunicCore.getShopAPI().registerRunicItemShop(fastTravelShop());
    }

    public RunicShopGeneric fastTravelShop() {
        Map<String, Integer> lowLevelReq = new HashMap<String, Integer>() {{
            put("coin", 5);
        }};
        Map<String, Integer> medLevelReq = new HashMap<String, Integer>() {{
            put("coin", 10);
        }};
        Map<String, Integer> highLevelReq = new HashMap<String, Integer>() {{
            put("coin", 15);
        }};
        LinkedHashSet<RunicShopItem> shopItems = new LinkedHashSet<>();

        shopItems.add
                (
                        new RunicShopItem(lowLevelReq,
                                wagonItem(TravelLocation.AZANA, "None"),
                                runWagonBuy(TravelLocation.AZANA),
                                Collections.singletonList(player -> hasAchievementRequirement(player, ""))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(lowLevelReq,
                                wagonItem(TravelLocation.KOLDORE, Achievement.DISCOVER_KOLDORE.getName()),
                                runWagonBuy(TravelLocation.KOLDORE),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_KOLDORE.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(lowLevelReq,
                                wagonItem(TravelLocation.WHALETOWN, Achievement.DISCOVER_WHALETOWN.getName()),
                                runWagonBuy(TravelLocation.WHALETOWN),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_WHALETOWN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(lowLevelReq,
                                wagonItem(TravelLocation.HILSTEAD, Achievement.DISCOVER_HILSTEAD.getName()),
                                runWagonBuy(TravelLocation.HILSTEAD),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_HILSTEAD.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(medLevelReq,
                                wagonItem(TravelLocation.WINTERVALE, Achievement.DISCOVER_WINTERVALE.getName()),
                                runWagonBuy(TravelLocation.WINTERVALE),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_WINTERVALE.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(medLevelReq,
                                wagonItem(TravelLocation.DEAD_MANS_REST, Achievement.DISCOVER_DEAD_MANS_REST.getName()),
                                runWagonBuy(TravelLocation.DEAD_MANS_REST),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_DEAD_MANS_REST.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(medLevelReq,
                                wagonItem(TravelLocation.ISFODAR, Achievement.DISCOVER_ISFODAR.getName()),
                                runWagonBuy(TravelLocation.ISFODAR),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_ISFODAR.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(medLevelReq,
                                wagonItem(TravelLocation.TIRNEAS, Achievement.DISCOVER_TIRENEAS.getName()),
                                runWagonBuy(TravelLocation.TIRNEAS),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_TIRENEAS.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(highLevelReq,
                                wagonItem(TravelLocation.ZENYTH, Achievement.DISCOVER_ZENYTH.getName()),
                                runWagonBuy(TravelLocation.ZENYTH),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_ZENYTH.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(highLevelReq,
                                wagonItem(TravelLocation.NAHEEN, Achievement.DISCOVER_NAHEEN.getName()),
                                runWagonBuy(TravelLocation.NAHEEN),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_NAHEEN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(highLevelReq,
                                wagonItem(TravelLocation.NAZMORA, Achievement.DISCOVER_NAZMORA.getName()),
                                runWagonBuy(TravelLocation.NAZMORA),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_NAZMORA.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(highLevelReq,
                                wagonItem(TravelLocation.STONEHAVEN, Achievement.DISCOVER_STONEHAVEN.getName()),
                                runWagonBuy(TravelLocation.STONEHAVEN),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_STONEHAVEN.getId()))
                        )
                );
        shopItems.add
                (
                        new RunicShopItem(highLevelReq,
                                wagonItem(TravelLocation.FROSTS_END, Achievement.DISCOVER_FROSTS_END.getName()),
                                runWagonBuy(TravelLocation.FROSTS_END),
                                Collections.singletonList(player -> hasAchievementRequirement(player, Achievement.DISCOVER_FROSTS_END.getId()))
                        )
                );
        shopItems.forEach(runicShopItem -> runicShopItem.setRemovePayment(true));
        return new RunicShopGeneric(45, ChatColor.YELLOW + "Wagonmaster", Arrays.asList(245, 246, 249, 256, 262, 267, 333, 272, 334, 285, 315, 337), shopItems);
    }

    /**
     * Adds a condition to the wagon vendor that checks for a specific achievement unlock
     *
     * @param achievementId the unique id of the achievement
     * @return true if achievement is unlocked
     */
    private boolean hasAchievementRequirement(Player player, String achievementId) {
        AchievementData achievementData = (AchievementData) RunicAchievements.getAPI().getSessionData(player.getUniqueId());
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievementId);
        if (!achievementId.equals("") && !achievementStatus.isUnlocked()) {
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5f, 1.0f);
            player.sendMessage(ChatColor.RED + "You must visit this location to unlock this fast travel!");
            return false;
        }
        return true;
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
