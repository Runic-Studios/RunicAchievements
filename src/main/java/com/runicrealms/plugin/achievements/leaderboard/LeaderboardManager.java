package com.runicrealms.plugin.achievements.leaderboard;

import co.aikar.taskchain.TaskChain;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.model.AchievementData;
import com.runicrealms.plugin.achievements.model.AchievementManager;
import com.runicrealms.plugin.achievements.util.AchievementUtil;
import com.runicrealms.plugin.common.util.ColorUtil;
import com.runicrealms.plugin.rdb.RunicDatabase;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Temporary strategy to display the top achievement scores using a MongoDB projection.
 * Should be moved into the Runic backend API to take the load off the server.
 *
 * @author Skyfallin
 */
public class LeaderboardManager {
    private static final int NAMES_TO_DISPLAY = 10;
    private static final int DELAY = 20; // Seconds
    private static final int REFRESH_SECONDS = 180; // Seconds
    private static final Location AZANA_LEADERBOARD = new Location(Bukkit.getWorld("Alterra"), -718.5, 39.5, 217.5);
    private static final Location STONEHAVEN_LEADERBOARD = new Location(Bukkit.getWorld("Alterra"), -814.5, 41.5, 767.5);

    public LeaderboardManager() {
        Hologram azanaHologram = HolographicDisplaysAPI.get(RunicAchievements.getInstance()).createHologram(AZANA_LEADERBOARD);
        Hologram stonehavenHologram = HolographicDisplaysAPI.get(RunicAchievements.getInstance()).createHologram(STONEHAVEN_LEADERBOARD);
        Set<Hologram> holograms = new HashSet<>();
        holograms.add(azanaHologram);
        holograms.add(stonehavenHologram);
        Bukkit.getScheduler().runTaskTimer(RunicAchievements.getInstance(),
                () -> createLeaderboardHologram(holograms), DELAY * 20L, REFRESH_SECONDS * 20L);
    }

    /**
     * Uses a TaskChain to async retrieve the leaderboard data, sort it, then create a hologram sync
     */
    public void createLeaderboardHologram(Set<Hologram> holograms) {
        TaskChain<?> chain = RunicAchievements.newChain();
        chain
                // Fetch data
                .asyncFirst(() -> {
                    Map<UUID, Integer> pointsMap = getAllAchievementPoints();
                    Set<UUID> operators = Bukkit.getOperators().stream().map(OfflinePlayer::getUniqueId).collect(Collectors.toUnmodifiableSet());
                    // Sort the map in descending order
                    return pointsMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .filter((entry) -> !operators.contains(entry.getKey()))
                            .limit(NAMES_TO_DISPLAY)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
                })
                .abortIfNull(AchievementManager.CONSOLE_LOG, null, "RunicAchievements failed to load the leaderboard!")
                .syncLast(sortedMap -> holograms.forEach(hologram -> updateHologram(sortedMap, hologram)))
                .execute();
    }

    private void updateHologram(Map<UUID, Integer> sortedMap, Hologram hologram) {
        Map<UUID, String> names = new ConcurrentHashMap<>();
        for (UUID uuid : sortedMap.keySet()) {
            LuckPermsProvider.get().getUserManager().loadUser(uuid).thenAccept(user -> {
                String nameColor = user.getCachedData().getMetaData().getMetaValue("name_color");
                if (nameColor == null) nameColor = "&7";
                nameColor = ColorUtil.format(nameColor);
                String name = user.getUsername();
                if (name == null) name = "Unknown";
                names.put(uuid, nameColor + name);
                if (names.size() == sortedMap.size()) {
                    Bukkit.getScheduler().runTask(RunicCore.getInstance(), () -> {
                        hologram.getLines().clear();
                        hologram.getLines().appendText(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "ACHIEVEMENT LEADERBOARD");
                        hologram.getLines().appendText("");
                        int rank = 1;
                        for (Map.Entry<UUID, Integer> entry : sortedMap.entrySet()) {
                            hologram.getLines().appendText(
                                    ChatColor.YELLOW.toString() + rank + ". " +
                                            names.get(entry.getKey()) + " " +
                                            ChatColor.YELLOW + "- " +
                                            ChatColor.GOLD + "[" + entry.getValue() + "]"
                            );
                            rank++;
                        }
                    });
                }
            });
        }
    }

    /**
     * Uses a projection to scan all MongoDB records in Achievements collection and extract the points value
     *
     * @return a map of player uuid to their total achievement points
     */
    public Map<UUID, Integer> getAllAchievementPoints() {
        Map<UUID, Integer> resultMap = new HashMap<>();
        MongoTemplate mongoTemplate = RunicDatabase.getAPI().getDataAPI().getMongoTemplate();
        List<AchievementData> achievementDataList = mongoTemplate.findAll(AchievementData.class);

        // Filter for only completed achievements
        for (AchievementData achievementData : achievementDataList) {
            UUID playerUuid = achievementData.getUuid();
            resultMap.put(playerUuid, AchievementUtil.calculateTotalAchievementPoints(achievementData));
        }

        return resultMap;
    }
}
