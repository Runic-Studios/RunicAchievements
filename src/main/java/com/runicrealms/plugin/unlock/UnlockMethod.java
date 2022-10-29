package com.runicrealms.plugin.unlock;

public abstract class UnlockMethod {

    private final AchievementType achievementType;

    public UnlockMethod(AchievementType achievementType) {
        this.achievementType = achievementType;
    }

    public AchievementType getAchievementType() {
        return achievementType;
    }
}
