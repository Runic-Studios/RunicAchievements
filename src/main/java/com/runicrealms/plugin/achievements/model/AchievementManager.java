package com.runicrealms.plugin.achievements.model;

import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainAbortAction;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.api.AchievementsDataAPI;
import com.runicrealms.plugin.common.api.AchievementsAPI;
import com.runicrealms.plugin.rdb.RunicDatabase;
import com.runicrealms.plugin.rdb.event.CharacterQuitEvent;
import com.runicrealms.plugin.rdb.event.CharacterSelectEvent;
import com.runicrealms.plugin.rdb.model.CharacterField;
import com.runicrealms.plugin.rdb.model.SessionDataNested;
import com.runicrealms.plugin.rdb.model.SessionDataNestedManager;
import org.bson.types.ObjectId;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class AchievementManager implements AchievementsDataAPI, Listener, SessionDataNestedManager, AchievementsAPI {
    public static final TaskChainAbortAction<Player, String, ?> CONSOLE_LOG = new TaskChainAbortAction<Player, String, Object>() {
        public void onAbort(TaskChain<?> chain, Player player, String message) {
            Bukkit.getLogger().log(Level.SEVERE, ChatColor.translateAlternateColorCodes('&', message));
        }
    };
    private final Map<Object, SessionDataNested> achievementDataMap; // Keyed by uuid

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
        String database = RunicDatabase.getAPI().getDataAPI().getMongoDatabase().getName();
        UUID uuid = (UUID) object;
        if (jedis.exists(database + ":" + uuid + ":hasAchievementData")) {
            return new AchievementData(uuid, jedis);
        }
        return null;
    }

    @Override
    public SessionDataNested getSessionData(Object object, int... slot) {
        UUID uuid = (UUID) object;
        // Check if achievement data is memoized
        return achievementDataMap.get(uuid);
    }

    @Override
    public boolean hasAchievement(Player player, String achievementId) {
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(player.getUniqueId());
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievementId);
        if (!achievementId.equals("") && !achievementStatus.isUnlocked()) {
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5f, 1.0f);
            player.sendMessage(ChatColor.RED + "You must visit this location to unlock this fast travel!");
            return false;
        }
        return true;
    }

    @Override
    public Map<Object, SessionDataNested> getSessionDataMap() {
        return this.achievementDataMap;
    }

    @Override
    public SessionDataNested loadSessionData(Object object, int... ignored) {
        UUID uuid = (UUID) object;
        try (Jedis jedis = RunicDatabase.getAPI().getRedisAPI().getNewJedisResource()) {
            // Step 1: Check if achievement data is cached in redis
            AchievementData achievementData = (AchievementData) checkJedisForSessionData(uuid, jedis);
            if (achievementData != null) return achievementData;
            // Step 2: Check the mongo database
            Query query = new Query();
            query.addCriteria(Criteria.where(CharacterField.PLAYER_UUID.getField()).is(uuid));
            MongoTemplate mongoTemplate = RunicDatabase.getAPI().getDataAPI().getMongoTemplate();
            List<AchievementData> results = mongoTemplate.find(query, AchievementData.class);
            if (results.size() > 0) {
                AchievementData result = results.get(0);
                result.writeToJedis(jedis);
                return result;
            }
            // Step 3: If no data is found, we create some data and save it to the collection
            AchievementData newData = new AchievementData(new ObjectId(), uuid);
            newData.addDocumentToMongo();
            newData.writeToJedis(jedis);
            return newData;
        }
    }

    @EventHandler
    public void onCharacterQuit(CharacterQuitEvent event) {
        AchievementData achievementData = (AchievementData) getSessionData(event.getPlayer().getUniqueId());
        TaskChain<?> chain = RunicAchievements.newChain();
        chain
                .asyncFirst(() -> {
                    try (Jedis jedis = RunicDatabase.getAPI().getRedisAPI().getNewJedisResource()) {
                        achievementData.writeToJedis(jedis);
                    }
                    return achievementData;
                })
                .abortIfNull(AchievementManager.CONSOLE_LOG, null, "RunicAchievements failed to write on quit!")
                .syncLast(data -> achievementDataMap.remove(event.getPlayer().getUniqueId()))
                .execute();
    }

    @EventHandler
    public void onCharacterSelect(CharacterSelectEvent event) {
        // For benchmarking
        long startTime = System.nanoTime();
        event.getPluginsToLoadData().add("achievements");
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        TaskChain<?> chain = RunicAchievements.newChain();
        chain
                .asyncFirst(() -> loadSessionData(uuid))
                .abortIfNull(AchievementManager.CONSOLE_LOG, player, "RunicAchievements failed to load on select!")
                .syncLast(achievementData -> {
                    getSessionDataMap().put(uuid, achievementData); // add to in-game memory
                    event.getPluginsToLoadData().remove("achievements");
                    // Calculate elapsed time
                    long endTime = System.nanoTime();
                    long elapsedTime = endTime - startTime;
                    // Log elapsed time in milliseconds
                    Bukkit.getLogger().info("RunicAchievements took: " + elapsedTime / 1_000_000 + "ms to load");
                })
                .execute();
    }

    @EventHandler(priority = EventPriority.LOW) // early
    public void onMongoSave(MongoSaveEvent event) {
        // Cancel the task timer
        RunicAchievements.getMongoTask().getTask().cancel();
        // Manually save all data (flush players marked for save)
        RunicAchievements.getMongoTask().saveAllToMongo(() -> event.markPluginSaved("achievements"));
    }
}
