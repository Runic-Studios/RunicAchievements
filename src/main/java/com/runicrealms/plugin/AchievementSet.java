package com.runicrealms.plugin;

public enum AchievementSet {

    NONE("None"),
    MASTER_GATHERER("Master Gatherer"),
    EXPLORER("Explorer");

    private final String name;

    AchievementSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static int getTotalAchievementsInSet(AchievementSet achievementSet) {
        int result = 0;
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getAchievementSet() != achievementSet) continue;
            result += 1;
        }
        return result;
    }
}
