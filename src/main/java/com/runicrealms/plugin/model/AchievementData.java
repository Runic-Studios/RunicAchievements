package com.runicrealms.plugin.model;

import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.database.PlayerMongoData;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AchievementData implements SessionData {

    private final UUID uuid;
    private final List<AchievementStatus> achievementStatusList;

    public AchievementData(UUID uuid, List<AchievementStatus> achievementStatusList) {
        this.uuid = uuid;
        this.achievementStatusList = achievementStatusList;
    }

    @Override
    public Map<String, String> toMap() {
        return null;
    }

    @Override
    public void writeToMongo(PlayerMongoData playerMongoData, int... ints) {

    }
}
