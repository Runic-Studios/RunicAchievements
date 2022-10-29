package com.runicrealms.plugin.unlocks;

public class UnlockMethod {

    private final AchievementType achievementType;

    public UnlockMethod(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }
}
