package com.runicrealms.plugin;

import com.runicrealms.plugin.rewards.ItemReward;
import com.runicrealms.plugin.rewards.Reward;

import java.util.Collections;
import java.util.List;

public enum Achievement {

    FISH_10_COD
            (
                    "fish-10-cod",
                    "Fish 10 Cod",
                    "Obtained by fishing 10 cod!",
                    10,
                    AchievementType.PROGRESS,
                    Collections.singletonList(new ItemReward("OakWood", 10)),
                    AchievementSet.NONE
            );

    private final String id;
    private final String name;
    private final String description;
    private final int pointValue;
    private final AchievementType achievementType;
    private final List<Reward> rewards;
    private final AchievementSet achievementSet;

    Achievement(String id, String name, String description, int pointValue, AchievementType achievementType,
                List<Reward> rewards, AchievementSet achievementSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        this.achievementType = achievementType;
        this.rewards = rewards;
        this.achievementSet = achievementSet;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPointValue() {
        return pointValue;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public AchievementSet getAchievementSet() {
        return achievementSet;
    }
}
