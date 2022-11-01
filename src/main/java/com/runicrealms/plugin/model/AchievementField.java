package com.runicrealms.plugin.model;

public enum AchievementField {

    PROGRESS("progress"),
    IS_UNLOCKED("isUnlocked");

    private final String field;

    AchievementField(String field) {
        this.field = field;
    }

    /**
     * Returns the corresponding RedisField from the given string version
     *
     * @param field a string matching a constant
     * @return the constant
     */
    public static AchievementField getFromFieldString(String field) {
        for (AchievementField achievementField : AchievementField.values()) {
            if (achievementField.getField().equalsIgnoreCase(field))
                return achievementField;
        }
        return null;
    }

    public String getField() {
        return field;
    }
}
