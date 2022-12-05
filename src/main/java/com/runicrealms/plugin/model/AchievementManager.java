package com.runicrealms.plugin.model;

import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.character.api.CharacterQuitEvent;
import com.runicrealms.plugin.character.api.CharacterSelectEvent;
import com.runicrealms.plugin.database.PlayerMongoData;
import com.runicrealms.plugin.database.event.MongoSaveEvent;
import com.runicrealms.plugin.model.callbacks.ReadCallbackNested;
import com.runicrealms.plugin.redis.RedisUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AchievementManager implements Listener, SessionDataNestedManager {

    private final Map<Object, SessionDataNested> achievementDataMap; // keyed by uuid

    public AchievementManager() {
        achievementDataMap = new HashMap<>();
        RunicAchievements.getInstance().getServer().getPluginManager().registerEvents(this, RunicAchievements.getInstance());
    }

    /**
     * Checks redis to see if the currently selected character's achievement data is cached.
     * And if it is, returns the AchievementData object
     *
     * @param object uuid of player to check
     * @param jedis  the jedis resource
     * @return a AchievementData object if it is found in redis
     */
    @Override
    public SessionDataNested checkJedisForSessionData(Object object, Jedis jedis, int... slot) {
        UUID uuid = (UUID) object;
        if (!RedisUtil.getNestedKeys(uuid + ":" + AchievementData.DATA_SECTION_ACHIEVEMENTS, jedis).isEmpty()) {
            return new AchievementData(uuid, jedis);
        }
        return null;
    }

    @Override
    public Map<Object, SessionDataNested> getSessionDataMap() {
        return this.achievementDataMap;
    }

    /**
     * Tries to retrieve an AchievementData object from server memory, otherwise falls back to redis / mongo
     *
     * @param object uuid of the player
     * @return an AchievementData object
     */
    @Override
    public SessionDataNested loadSessionData(Object object, int... slot) {
        UUID uuid = (UUID) object;
        // Step 1: check if achievement data is memoized
        return achievementDataMap.get(uuid);
    }

    /**
     * Creates an AchievementData object. Tries to build it from session storage (Redis) first,
     * then falls back to Mongo
     *
     * @param object uuid of player who is attempting to load their data
     */
    @Override
    public void loadSessionData(Object object, Jedis jedis, ReadCallbackNested callback, int... slot) {
        Bukkit.getScheduler().runTaskAsynchronously(RunicAchievements.getInstance(), () -> {
            UUID uuid = (UUID) object;
            // Step 1: check if achievement data is cached in redis
            AchievementData achievementDataRedis = (AchievementData) checkJedisForSessionData(uuid, jedis);
            if (achievementDataRedis != null) {
                Bukkit.getScheduler().runTask(RunicCore.getInstance(), () -> callback.onQueryComplete(achievementDataRedis));
            }
            // Step 2: check mongo documents
            else {
                AchievementData achievementDataMongo = new AchievementData(uuid, new PlayerMongoData(uuid.toString()), jedis);
                Bukkit.getScheduler().runTask(RunicCore.getInstance(), () -> callback.onQueryComplete(achievementDataMongo));
            }
        });
    }

    @EventHandler
    public void onCharacterQuit(CharacterQuitEvent event) {
        AchievementData achievementData = (AchievementData) loadSessionData(event.getPlayer().getUniqueId());
        achievementData.writeToJedis(event.getJedis());
        achievementDataMap.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onCharacterSelect(CharacterSelectEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        loadSessionData(uuid, event.getJedis(), sessionDataNested -> {
        });
    }

    @EventHandler(priority = EventPriority.LOW) // early
    public void onMongoSave(MongoSaveEvent event) {
        UUID lastKey = event.getPlayersToSave().lastKey();
        for (UUID uuid : event.getPlayersToSave().keySet()) {
            PlayerMongoData playerMongoData = event.getPlayersToSave().get(uuid).getPlayerMongoData();
            loadSessionData(uuid, event.getJedis(), sessionDataNested -> {
                sessionDataNested.writeToMongo(playerMongoData);
                if (uuid.equals(lastKey)) {
                    event.markPluginSaved("achievements");
                }
            });
        }
    }
}
