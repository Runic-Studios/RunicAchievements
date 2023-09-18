package com.runicrealms.plugin.achievements;

public enum AchievementSet {

    NONE("None"),
    COMBATANT("Combatant"),
    EXPLORER("Explorer"),
    MASTER_GATHERER("Master Gatherer"),
    SLAYER("Slayer");

    private final String name;

    AchievementSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static int getTotalAchievementsInSet(AchievementSet achievementSet, boolean displayedOnly) {
        int result = 0;
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getAchievementSet() != achievementSet || (displayedOnly && !achievement.shouldDisplay())) {
                continue;
            }
            result += 1;
        }
        return result;
    }
}
