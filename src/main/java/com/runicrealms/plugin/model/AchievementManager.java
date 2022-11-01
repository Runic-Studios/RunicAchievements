package com.runicrealms.plugin.model;

import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.RunicCoreAPI;
import com.runicrealms.plugin.character.api.CharacterQuitEvent;
import com.runicrealms.plugin.character.api.CharacterSelectEvent;
import com.runicrealms.plugin.database.PlayerMongoData;
import com.runicrealms.plugin.database.event.MongoSaveEvent;
import com.runicrealms.plugin.redis.RedisUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;

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
        UUID uuid = event.getPlayer().getUniqueId();
        loadAchievementData(uuid);
    }

    @EventHandler
    public void onCharacterQuit(CharacterQuitEvent event) {
        AchievementData achievementData = loadAchievementData(event.getPlayer().getUniqueId());
        achievementData.writeToJedis(event.getJedis());
        achievementDataMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onMongoSave(MongoSaveEvent event) {
        for (UUID uuid : event.getPlayersToSave().keySet()) {
            PlayerMongoData playerMongoData = event.getPlayersToSave().get(uuid).getPlayerMongoData();
            AchievementData achievementData = loadAchievementData(uuid);
            achievementData.writeToMongo(playerMongoData);
        }
        event.markPluginSaved("achievements");
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
        // Step 2: check if achievement data is cached in redis
        try (Jedis jedis = RunicCoreAPI.getNewJedisResource()) {
            return loadAchievementData(uuid, jedis);
        }
    }

    /**
     * Creates an AchievementData object. Tries to build it from session storage (Redis) first,
     * then falls back to Mongo
     *
     * @param uuid of player who is attempting to load their data
     */
    public AchievementData loadAchievementData(UUID uuid, Jedis jedis) {
        // Step 2: check if achievement data is cached in redis
        AchievementData achievementData = checkRedisForAchievementData(uuid, jedis);
        if (achievementData != null) return achievementData;
        // Step 2: check mongo documents
        PlayerMongoData playerMongoData = new PlayerMongoData(uuid.toString());
        return new AchievementData(uuid, playerMongoData, jedis);
    }

    /**
     * Checks redis to see if the currently selected character's achievement data is cached.
     * And if it is, returns the AchievementData object
     *
     * @param uuid  of player to check
     * @param jedis the jedis resource
     * @return a AchievementData object if it is found in redis
     */
    public AchievementData checkRedisForAchievementData(UUID uuid, Jedis jedis) {
        if (!RedisUtil.getNestedKeys(uuid + ":" + AchievementData.DATA_SECTION_ACHIEVEMENTS, jedis).isEmpty()) {
            Bukkit.broadcastMessage(ChatColor.GREEN + "redis achievement data found, building data from redis");
            return new AchievementData(uuid, jedis);
        }
        Bukkit.broadcastMessage(ChatColor.RED + "redis achievement data not found");
        return null;
    }

    public Map<UUID, AchievementData> getAchievementDataMap() {
        return achievementDataMap;
    }
}
