package com.runicrealms.plugin.achievements.model;

import com.runicrealms.plugin.achievements.Achievement;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.rdb.RunicDatabase;
import com.runicrealms.plugin.rdb.model.SessionDataMongo;
import com.runicrealms.plugin.rdb.model.SessionDataNested;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Document(collection = "achievements")
public class AchievementData implements SessionDataMongo, SessionDataNested {
    public static final String DATA_SECTION_ACHIEVEMENTS = "achievements";
    public static final List<String> FIELDS = new ArrayList<String>() {{
        add(AchievementField.PROGRESS.getField());
        add(AchievementField.IS_UNLOCKED.getField());
    }};
    @Id
    private ObjectId id;
    @Field("playerUuid")
    private UUID uuid;
    private Map<String, AchievementStatus> achievementStatusMap;

    @SuppressWarnings("unused")
    public AchievementData() {
        // Default constructor for Spring
    }

    /**
     * Constructor for new players
     */
    public AchievementData(ObjectId id, UUID uuid) {
        this.id = id;
        this.uuid = uuid;
        this.achievementStatusMap = new HashMap<>();
        for (Achievement achievement : Achievement.values()) { // Load blank values for achievements w/ no data to prevent npe
            achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
        }
    }

    /**
     * AchievementData binds player's uuid to a map of achievement id's to their current status, used to track their progress
     *
     * @param uuid of the player
     */
    public AchievementData(UUID uuid, Jedis jedis) {
        this.uuid = uuid;
        String database = RunicDatabase.getAPI().getDataAPI().getMongoDatabase().getName();
        String rootKey = database + ":" + uuid + ":" + DATA_SECTION_ACHIEVEMENTS;

        this.achievementStatusMap = new HashMap<>();

        for (String key : jedis.keys(rootKey + ":*")) {
            int lastIndex = key.lastIndexOf(':');
            String achievementId = lastIndex != -1 ? key.substring(lastIndex + 1) : key;
            Map<String, String> fieldsMap = getDataMapFromJedis(jedis, achievementId);
            AchievementStatus achievementStatus = new AchievementStatus
                    (
                            uuid,
                            Achievement.getFromId(achievementId),
                            Integer.parseInt(fieldsMap.get(AchievementField.PROGRESS.getField())),
                            Boolean.parseBoolean(fieldsMap.get(AchievementField.IS_UNLOCKED.getField()))
                    );
            achievementStatusMap.put(achievementId, achievementStatus);
        }

        for (Achievement achievement : Achievement.values()) { // Load blank values for achievements w/ no data to prevent npe
            if (achievementStatusMap.get(achievement.getId()) != null)
                continue; // Ignore where there is data
            achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public AchievementData addDocumentToMongo() {
        MongoTemplate mongoTemplate = RunicDatabase.getAPI().getDataAPI().getMongoTemplate();
        return mongoTemplate.save(this);
    }

    public Map<String, AchievementStatus> getAchievementStatusMap() {
        return achievementStatusMap;
    }

    public void setAchievementStatusMap(Map<String, AchievementStatus> achievementStatusMap) {
        this.achievementStatusMap = achievementStatusMap;
    }

    @Override
    public Map<String, String> getDataMapFromJedis(Jedis jedis, Object nestedObject, int... slot) { // don't need slot, achievements are acc-wide
        String achievementId = (String) nestedObject;
        String database = RunicDatabase.getAPI().getDataAPI().getMongoDatabase().getName();
        String achievementKey = database + ":" + this.uuid + ":" + DATA_SECTION_ACHIEVEMENTS + ":" + achievementId;
        return jedis.hgetAll(achievementKey);
    }

    @Override
    public List<String> getFields() {
        return FIELDS;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Returns a map that can be used to set nested values in redis
     *
     * @param nestedObject a single achievement
     * @return a map of string keys and character info values
     */
    @Override
    public Map<String, String> toMap(Object nestedObject) {
        AchievementStatus achievementStatus = (AchievementStatus) nestedObject;
        return new HashMap<String, String>() {{
            put("isUnlocked", String.valueOf(achievementStatus.isUnlocked()));
            put("progress", String.valueOf(achievementStatus.getProgress()));
        }};
    }

    @Override
    public void writeToJedis(Jedis jedis, int... ignored) {
        String database = RunicDatabase.getAPI().getDataAPI().getMongoDatabase().getName();
        // Inform the server that this player should be saved to mongo on next task (jedis data is refreshed)
        jedis.sadd(database + ":" + "markedForSave:achievements", this.uuid.toString());
        // Ensure the system knows that there is data in redis
        boolean hasAchievementData = false;
        String uuid = String.valueOf(this.uuid);
        String key = uuid + ":" + DATA_SECTION_ACHIEVEMENTS;
        for (String achievementId : achievementStatusMap.keySet()) {
            AchievementStatus status = achievementStatusMap.get(achievementId);
            if (!status.isUnlocked() && status.getProgress() == 0)
                continue; // Ignore achievements w/ no data
            hasAchievementData = true;
            jedis.hmset(database + ":" + key + ":" + achievementId, this.toMap(achievementStatusMap.get(achievementId)));
            jedis.expire(database + ":" + key + ":" + achievementId, RunicDatabase.getAPI().getRedisAPI().getExpireTime());
        }
        if (hasAchievementData) {
            jedis.set(database + ":" + this.uuid + ":hasAchievementData", "true");
            jedis.expire(database + ":" + this.uuid + ":hasAchievementData", RunicDatabase.getAPI().getRedisAPI().getExpireTime());
        }
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
}
