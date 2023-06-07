package com.runicrealms.plugin.leaderboard;

import co.aikar.taskchain.TaskChain;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.model.AchievementManager;
import com.runicrealms.plugin.rdb.RunicDatabase;
import com.runicrealms.plugin.util.AchievementUtil;
import me.clip.placeholderapi.PlaceholderAPI;
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
        Hologram azanaHologram = HologramsAPI.createHologram(RunicAchievements.getInstance(), AZANA_LEADERBOARD);
        Hologram stonehavenHologram = HologramsAPI.createHologram(RunicAchievements.getInstance(), STONEHAVEN_LEADERBOARD);
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
                    // Sort the map in descending order
                    return pointsMap.entrySet().stream()
                            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                            .limit(NAMES_TO_DISPLAY)
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
                })
                .abortIfNull(AchievementManager.CONSOLE_LOG, null, "RunicAchievements failed to load the leaderboard!")
                .syncLast(sortedMap -> holograms.forEach(hologram -> updateHologram(sortedMap, hologram)))
                .execute();
    }

    private void updateHologram(Map<UUID, Integer> sortedMap, Hologram hologram) {
        hologram.clearLines();
        hologram.appendTextLine(ChatColor.GOLD + String.valueOf(ChatColor.BOLD) + "ACHIEVEMENT LEADERBOARD");
        hologram.appendTextLine("");
        int rank = 1;
        for (Map.Entry<UUID, Integer> entry : sortedMap.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getKey());
            String nameColorPlaceholder = PlaceholderAPI.setPlaceholders(player, "%luckperms_meta_name_color%");
            String line = String.format
                    (
                            ChatColor.YELLOW + "%d. " +
                                    ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', nameColorPlaceholder) + "%s " +
                                    ChatColor.YELLOW + "- " +
                                    ChatColor.GOLD + "[%d]",
                            rank,
                            player.getName(),
                            entry.getValue()
                    );
            hologram.appendTextLine(line);
            rank++;
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
