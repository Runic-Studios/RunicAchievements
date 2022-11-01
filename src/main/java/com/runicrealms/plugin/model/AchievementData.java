package com.runicrealms.plugin.model;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.RunicCoreAPI;
import com.runicrealms.plugin.database.PlayerMongoData;
import com.runicrealms.plugin.database.PlayerMongoDataSection;
import com.runicrealms.plugin.redis.RedisUtil;
import redis.clients.jedis.Jedis;

import java.util.*;

public class AchievementData implements SessionDataNested {
    public static final String DATA_SECTION_ACHIEVEMENTS = "achievements";
    static List<String> fields = new ArrayList<String>() {{
        add(AchievementField.PROGRESS.getField());
        add(AchievementField.IS_UNLOCKED.getField());
    }};
    private final UUID uuid;
    private final Map<String, AchievementStatus> achievementStatusMap;

    /**
     * @param uuid
     * @param playerMongoData
     * @param jedis
     */
    public AchievementData(UUID uuid, PlayerMongoData playerMongoData, Jedis jedis) {
        this.uuid = uuid;
        this.achievementStatusMap = new HashMap<>();
        for (Achievement achievement : Achievement.values()) {

            if (playerMongoData.get(DATA_SECTION_ACHIEVEMENTS + "." + achievement.getId()) != null) {
                PlayerMongoDataSection achievementsSection = (PlayerMongoDataSection) playerMongoData.get(DATA_SECTION_ACHIEVEMENTS);
                int progress = getProgressFromDataSection(achievementsSection, achievement.getId());
                boolean isUnlocked = getIsUnlockedFromDataSection(achievementsSection, achievement.getId());
                achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement, progress, isUnlocked));
            } else {
                achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
            }
        }

        this.writeToJedis(jedis);
        RunicAchievements.getAchievementManager().getAchievementDataMap().put(uuid, this); // add to in-game memory
    }

    /**
     * @param uuid
     * @param jedis
     */
    public AchievementData(UUID uuid, Jedis jedis) {
        this.uuid = uuid;
        String key = uuid + ":" + DATA_SECTION_ACHIEVEMENTS;


        this.achievementStatusMap = new HashMap<>();
        for (Achievement achievement : Achievement.values()) { // load blank values
            achievementStatusMap.put(achievement.getId(), new AchievementStatus(uuid, achievement));
        }

        for (String achievementIdKey : RedisUtil.getNestedKeys(key, jedis)) {

            String[] split = achievementIdKey.split(":");
            String achievementId = split[2];
            Map<String, String> fieldsMap = new HashMap<>();
            String[] fieldsToArray = AchievementData.fields.toArray(new String[0]);
            List<String> values = jedis.hmget(key + ":" + achievementId, fieldsToArray);
            for (int i = 0; i < fieldsToArray.length; i++) {
                fieldsMap.put(fieldsToArray[i], values.get(i));
            }

            AchievementStatus achievementStatus = new AchievementStatus
                    (
                            uuid,
                            Achievement.getFromId(achievementId),
                            Integer.parseInt(fieldsMap.get(AchievementField.PROGRESS.getField())),
                            Boolean.parseBoolean(AchievementField.IS_UNLOCKED.getField())
                    );
            achievementStatusMap.put(achievementId, achievementStatus);
        }
        RunicAchievements.getAchievementManager().getAchievementDataMap().put(uuid, this); // add to in-game memory
    }

    public static List<String> getFields() {
        return fields;
    }

    /**
     * @param achievementsSection
     * @param achievementId
     * @return
     */
    private int getProgressFromDataSection(PlayerMongoDataSection achievementsSection, String achievementId) {
        if (achievementsSection.get(achievementId + "." + AchievementField.PROGRESS.getField(), Integer.class) != null) {
            return achievementsSection.get(achievementId + "." + AchievementField.PROGRESS.getField(), Integer.class);
        } else {
            return 0;
        }
    }

    /**
     * @param achievementsSection
     * @param achievementId
     * @return
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
        String achievementKey = this.uuid + ":" + DATA_SECTION_ACHIEVEMENTS + achievementId;
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
    public PlayerMongoData writeToMongo(PlayerMongoData playerMongoData, Jedis jedis, int... slot) { // don't need slot, achievements are acc-wide
        PlayerMongoDataSection achievementSection;
        for (String achievementId : RunicCoreAPI.getNestedJedisKeys(uuid + ":" + DATA_SECTION_ACHIEVEMENTS, jedis)) {
            achievementSection = (PlayerMongoDataSection) playerMongoData.getSection(DATA_SECTION_ACHIEVEMENTS + "." + achievementId);
            Map<String, String> dataMap = getDataMapFromJedis(jedis, achievementId);
            for (String achievementDatum : dataMap.keySet()) {
                achievementSection.set(achievementDatum, dataMap.get(achievementDatum));
            }
        }
        return playerMongoData;
    }
}
