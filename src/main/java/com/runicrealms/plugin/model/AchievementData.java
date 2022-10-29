package com.runicrealms.plugin.model;

import com.runicrealms.plugin.database.PlayerMongoData;

import java.util.Map;

public class AchievementData implements SessionData {


    @Override
    public Map<String, String> toMap() {
        return null;
    }

    @Override
    public void writeToMongo(PlayerMongoData playerMongoData, int... ints) {

    }
}
