package com.runicrealms.plugin.model;

import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.database.PlayerMongoData;

import java.util.Map;
import java.util.UUID;

public class AchievementData implements SessionData {

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

    @Override
    public Map<String, String> toMap() {
        return null;
    }

    @Override
    public void writeToMongo(PlayerMongoData playerMongoData, int... ints) {

    }
}
