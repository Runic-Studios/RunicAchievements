package com.runicrealms.plugin;

import java.util.UUID;

/**
 *
 */
public class AchievementStatus {

    private final UUID uuid;
    private final Achievement achievement;
    private int progress;
    private boolean isUnlocked;

    /**
     * @param uuid
     * @param achievement
     * @param progress
     * @param isUnlocked
     */
    public AchievementStatus(UUID uuid, Achievement achievement, int progress, boolean isUnlocked) {
        this.uuid = uuid;
        this.achievement = achievement;
        this.progress = progress;
        this.isUnlocked = isUnlocked;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isUnlocked() {
        return isUnlocked;
    }

    public void setUnlocked(boolean unlocked) {
        isUnlocked = unlocked;
    }
}
