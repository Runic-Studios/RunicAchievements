package com.runicrealms.plugin.model;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.character.api.CharacterSelectEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AchievementManager implements Listener {

    private final Map<UUID, AchievementData> achievementDataMap;

    public AchievementManager() {
        achievementDataMap = new HashMap<>();
        RunicAchievements.getInstance().getServer().getPluginManager().registerEvents(this, RunicAchievements.getInstance());
    }

    @EventHandler
    public void onCharacterSelect(CharacterSelectEvent event) {
        AchievementStatus dummy = new AchievementStatus(event.getPlayer().getUniqueId(), Achievement.FISH_10_COD, 0, false);
        Map<String, AchievementStatus> dummy2 = new HashMap<>();
        dummy2.put(Achievement.FISH_10_COD.getId(), dummy);
        achievementDataMap.put(event.getPlayer().getUniqueId(), new AchievementData(event.getPlayer().getUniqueId(), dummy2));
    }

    /**
     * Tries to retrieve an AchievementData object from server memory, otherwise falls back to redis / mongo
     *
     * @param uuid of the player
     * @return an AchievementData object
     */
    public AchievementData loadAchievementData(UUID uuid) {
        // Step 1: check if achievement data is memoized
        AchievementData achievementData = achievementDataMap.get(uuid);
        if (achievementData != null) return achievementData;
        return null;
//        // Step 2: check if achievement data is cached in redis
//        try (Jedis jedis = RunicCoreAPI.getNewJedisResource()) {
//            return loadAchievementData(uuid, jedis);
//        }
    }

//    /**
//     * Creates an AchievementData object. Tries to build it from session storage (Redis) first,
//     * then falls back to Mongo
//     *
//     * @param uuid of player who is attempting to load their data
//     */
//    public AchievementData loadAchievementData(UUID uuid, Jedis jedis) {
//        // Step 2: check if achievement data is cached in redis
//        AchievementData achievementData = checkRedisForAchievementData(uuid, jedis);
//        if (achievementData != null) return achievementData;
//        // Step 2: check mongo documents
//        PlayerMongoData playerMongoData = new PlayerMongoData(uuid.toString());
//        return new AchievementData(uuid, playerMongoData, jedis);
//    }
//
//    /**
//     * Checks redis to see if the currently selected character's achievement data is cached.
//     * And if it is, returns the AchievementData object
//     *
//     * @param uuid  of player to check
//     * @param jedis the jedis resource
//     * @return a AchievementData object if it is found in redis
//     */
//    public AchievementData checkRedisForAchievementData(UUID uuid, Jedis jedis) {
//        if (jedis.exists(uuid.toString())) {
//            Bukkit.broadcastMessage(ChatColor.GREEN + "redis achievement data found, building data from redis");
//            jedis.expire(uuid.toString(), RedisUtil.EXPIRE_TIME);
//            return new AchievementData(uuid, jedis);
//        }
//        Bukkit.broadcastMessage(ChatColor.RED + "redis achievement data not found");
//        return null;
//    }

    public Map<UUID, AchievementData> getAchievementDataMap() {
        return achievementDataMap;
    }
}
