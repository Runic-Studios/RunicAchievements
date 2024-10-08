package com.runicrealms.plugin.achievements.util;

import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.model.AchievementData;

public class AchievementUtil {

    /**
     * Calculates the total achievement points of the player
     *
     * @param achievementData their data wrapper object
     * @return points for each achievement
     */
    public static int calculateTotalAchievementPoints(AchievementData achievementData) {
        int totalPoints = 0;
        for (AchievementStatus achievementStatus : achievementData.getAchievementStatusMap().values()) {
            if (!achievementStatus.isUnlocked()) continue;
            totalPoints += achievementStatus.getAchievement().getPointValue();
        }
        return totalPoints;
    }

}
