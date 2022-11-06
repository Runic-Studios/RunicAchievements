package com.runicrealms.plugin.model;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.database.MongoData;
import com.runicrealms.plugin.database.PlayerMongoData;
import com.runicrealms.plugin.database.PlayerMongoDataSection;
import com.runicrealms.plugin.redis.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class AchievementData implements SessionDataNested {
    public static final String DATA_SECTION_ACHIEVEMENTS = "achievements";
    public static final List<String> FIELDS = new ArrayList<String>() {{
        add(AchievementField.PROGRESS.getField());
        add(AchievementField.IS_UNLOCKED.getField());
    }};
    private final UUID uuid;
    private final Map<String, AchievementStatus> achievementStatusMap;

    /**
     * AchievementData binds player's uuid to a map of achievement id's to their current status, used to track their progress
     *
     * @param uuid            of the player
     * @param playerMongoData of the login event
     * @param jedis           the jedis resource
     */
    public AchievementData(UUID uuid, PlayerMongoData playerMongoData, Jedis jedis) {
        this.uuid = uuid;
        this.achievementStatusMap = new HashMap<>();
        for (Achievement achievement : Achievement.values()) {

            if (playerMongoData.get(DATA_SECTION_ACHIEVEMENTS + "." + achievement.getId()) != null) {
                PlayerMongoDataSection achievementsSection = (PlayerMongoDataSection) playerMongoData.getSection(DATA_SECTION_ACHIEVEMENTS);
                int progress = getProgressFromDataSection(achievementsSection, achievement.getId());
                boolean isUnlocked = getIsUnlockedFromDataSection(achievementsSection, achievement.getId());
                achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement, progress, isUnlocked));
            } else {
                achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
            }
        }

        this.writeToJedis(jedis);
        RunicAchievements.getAchievementManager().getSessionDataMap().put(uuid, this); // add to in-game memory
    }

    /**
     * AchievementData binds player's uuid to a map of achievement id's to their current status, used to track their progress
     *
     * @param uuid  of the player
     * @param jedis the jedis resource
     */
    public AchievementData(UUID uuid, Jedis jedis) {
        this.uuid = uuid;
        String key = uuid + ":" + DATA_SECTION_ACHIEVEMENTS;

        this.achievementStatusMap = new HashMap<>();

        for (String achievementId : RedisUtil.getNestedKeys(key, jedis)) {
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

        for (Achievement achievement : Achievement.values()) { // load blank values for achievements w/ no data to prevent npe
            if (achievementStatusMap.get(achievement.getId()) != null) continue;
            achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
        }

        RunicAchievements.getAchievementManager().getSessionDataMap().put(uuid, this); // add to in-game memory
    }

    /**
     * Grabs the achievement progress matching id from the given achievement section of mongo
     *
     * @param achievementsSection the relevant section of the mongo data
     * @param achievementId       id of the achievement
     * @return the int progress value
     */
    private int getProgressFromDataSection(PlayerMongoDataSection achievementsSection, String achievementId) {
        if (achievementsSection.get(achievementId + "." + AchievementField.PROGRESS.getField(), Integer.class) != null) {
            return achievementsSection.get(achievementId + "." + AchievementField.PROGRESS.getField(), Integer.class);
        } else {
            return 0;
        }
    }

    /**
     * Grabs the achievement unlock boolean matching id from the given achievement section of mongo
     *
     * @param achievementsSection the relevant section of the mongo data
     * @param achievementId       id of the achievement
     * @return true if achievement is unlocked
     */
    private boolean getIsUnlockedFromDataSection(PlayerMongoDataSection achievementsSection, String achievementId) {
        if (achievementsSection.get(achievementId + "." + AchievementField.IS_UNLOCKED.getField(), Boolean.class) != null) {
            return achievementsSection.get(achievementId + "." + AchievementField.IS_UNLOCKED.getField(), Boolean.class);
        } else {
            return false;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<String, AchievementStatus> getAchievementStatusMap() {
        return achievementStatusMap;
    }

    @Override
    public List<String> getFields() {
        return FIELDS;
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
    public Map<String, String> getDataMapFromJedis(Jedis jedis, Object nestedObject, int... slot) { // don't need slot, achievements are acc-wide
        String achievementId = (String) nestedObject;
        String achievementKey = this.uuid + ":" + DATA_SECTION_ACHIEVEMENTS + ":" + achievementId;
        return jedis.hgetAll(achievementKey);
    }

    @Override
    public void writeToJedis(Jedis jedis, int... slot) { // don't need slot, achievements are acc-wide
        String uuid = String.valueOf(this.uuid);
        String key = uuid + ":" + DATA_SECTION_ACHIEVEMENTS;
        for (String achievementId : achievementStatusMap.keySet()) {
            jedis.hmset(key + ":" + achievementId, this.toMap(achievementStatusMap.get(achievementId)));
            jedis.expire(key + ":" + achievementId, RedisUtil.EXPIRE_TIME);
        }
    }

    @Override
    public MongoData writeToMongo(MongoData mongoData, int... slot) { // don't need slot, achievements are acc-wide
        PlayerMongoData playerMongoData = (PlayerMongoData) mongoData;
        PlayerMongoDataSection achievementSection;
        for (String achievementId : this.achievementStatusMap.keySet()) {
            achievementSection = (PlayerMongoDataSection) playerMongoData.getSection(DATA_SECTION_ACHIEVEMENTS + "." + achievementId);
            achievementSection.set(AchievementField.PROGRESS.getField(), this.achievementStatusMap.get(achievementId).getProgress());
            achievementSection.set(AchievementField.IS_UNLOCKED.getField(), this.achievementStatusMap.get(achievementId).isUnlocked());
        }
        return playerMongoData;
    }
}
