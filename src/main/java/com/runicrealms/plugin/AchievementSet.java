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
}
