package com.runicrealms.plugin.model;

import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.database.PlayerMongoData;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AchievementData implements SessionDataNested {

    private static final String DATA_SECTION_ACHIEVEMENTS = "achievements";
    private final UUID uuid;
    private final Map<String, AchievementStatus> achievementStatusList;

    public AchievementData(UUID uuid, Map<String, AchievementStatus> achievementStatusList) {
        this.uuid = uuid;
        this.achievementStatusList = achievementStatusList;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Map<String, AchievementStatus> getAchievementStatusList() {
        return achievementStatusList;
    }

    /**
     * @param nestedObject
     * @return
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
    public void writeToJedis(Jedis jedis, int... slot) {
        String uuid = String.valueOf(this.uuid);
        String key = uuid + ":" + DATA_SECTION_ACHIEVEMENTS;
        for (String achievementId : achievementStatusList.keySet()) {
            jedis.hmset(key + ":" + achievementId, this.toMap(achievementStatusList.get(achievementId)));
        }
    }

    @Override
    public void writeToMongo(PlayerMongoData playerMongoData, int... slot) {

    }
}
