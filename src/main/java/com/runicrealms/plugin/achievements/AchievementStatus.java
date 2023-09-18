package com.runicrealms.plugin.achievements;

import java.util.UUID;

/**
 * A wrapper for an Achievement enum that also contains a player their progress
 */
public class AchievementStatus {
    private UUID uuid;
    private Achievement achievement;
    private int progress;
    private boolean isUnlocked;

    @SuppressWarnings("unused")
    public AchievementStatus() {
        // Default constructor for Spring
    }

    /**
     * A wrapper for an Achievement enum that also contains a player their progress
     *
     * @param uuid        of the player
     * @param achievement from the enumerated list
     */
    public AchievementStatus(UUID uuid, Achievement achievement) {
        this.uuid = uuid;
        this.achievement = achievement;
        this.progress = 0;
        this.isUnlocked = false;
    }

    /**
     * A wrapper for an Achievement enum that also contains a player their progress
     *
     * @param uuid        of the player
     * @param achievement from the enumerated list
     * @param progress    of the achievement (50 cod, etc.)
     * @param isUnlocked  whether the achievement has been unlocked
     */
    public AchievementStatus(UUID uuid, Achievement achievement, int progress, boolean isUnlocked) {
        this.uuid = uuid;
        this.achievement = achievement;
        this.progress = progress;
        this.isUnlocked = isUnlocked;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
