package com.runicrealms.plugin.listeners;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.events.AchievementUnlockEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.professions.event.GatheringEvent;
import com.runicrealms.plugin.professions.gathering.GatheringResource;
import com.runicrealms.plugin.unlocks.ProgressUnlock;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class FishingAchievementListener implements Listener {

    @EventHandler
    public void onGatherFish(GatheringEvent event) {
        if (event.getGatheringResource() != GatheringResource.COD) return;
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        if (achievementData.getAchievementStatusList().get(Achievement.FISH_10_COD.getId()).isUnlocked()) return;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(Achievement.FISH_10_COD.getId());
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + 1);
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        // if the player is set to unlock achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(event.getPlayer(), achievementStatus.getAchievement());
            Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
        } else {
            Bukkit.broadcastMessage("achievement progress for cod is now: " + achievementStatus.getProgress());
        }
    }
}
