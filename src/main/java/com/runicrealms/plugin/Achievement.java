package com.runicrealms.plugin;

public enum Achievement {

    FISH_10_COD();

    private final String id;
    private final String name;
    private final String description;
    private final int pointValue;
    private final AchievementType achievementType;
    private final RewardType rewardType;

    Achievement(String id, String name, String description, int pointValue, AchievementType achievementType, RewardType rewardType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        this.achievementType = achievementType;
        this.rewardType = rewardType;
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

    public RewardType getRewardType() {
        return rewardType;
    }
}
