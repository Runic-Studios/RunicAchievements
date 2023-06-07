package com.runicrealms.plugin.util;

import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.model.AchievementData;

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
