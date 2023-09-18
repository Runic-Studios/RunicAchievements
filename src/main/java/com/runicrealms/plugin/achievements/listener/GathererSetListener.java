package com.runicrealms.plugin.achievements.listener;

import com.runicrealms.plugin.achievements.Achievement;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.achievements.model.AchievementData;
import com.runicrealms.plugin.achievements.unlock.ProgressUnlock;
import com.runicrealms.plugin.professions.event.GatheringEvent;
import com.runicrealms.plugin.professions.gathering.GatheringResource;
import com.runicrealms.plugin.professions.listeners.HarvestingListener;
import io.lumine.mythic.bukkit.events.MythicMobDeathEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    public void onHarvest(MythicMobDeathEvent event) {
        String faction = event.getMob().getFaction();
        if (faction == null) return;
        if (!faction.equals(HarvestingListener.HERB_FACTION)) return;
        Achievement achievement = Achievement.SMELL_ROSES;
        foo(event.getKiller().getUniqueId(), achievement);
    }

    @EventHandler
    public void onGather(GatheringEvent event) {
        for (GatheringResource resource : event.getGatheringResources()) {
            Achievement achievement = Achievement.getFromRunicItemId(resource.getTemplateId());
            if (achievement == null) {
                continue; // Ensure some achievement listens for this resource
            }

            foo(event.getPlayer().getUniqueId(), achievement);
        }
    }

    private void foo(UUID uuid, Achievement achievement) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
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
            AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(player, achievementStatus.getAchievement());
            Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
        }
    }
}
