package com.runicrealms.plugin.listener;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.professions.event.GatheringEvent;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

/**
 * Used to handle achievements based on gathering a material
 *
 * @author Skyfallin
 */
public class GathererSetListener implements Listener {

    @EventHandler
    public void onGather(GatheringEvent event) {

        Achievement achievement = Achievement.getFromRunicItemId(event.getGatheringResource().getTemplateId());
        if (achievement == null) return; // Ensure some achievement listens for this resource

        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return; // Ignore completed achievements

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + 1);
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            achievementStatus.setUnlocked(true);
            AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(event.getPlayer(), achievementStatus.getAchievement());
            Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
        }
    }
}
