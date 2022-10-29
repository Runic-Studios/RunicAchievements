package com.runicrealms.plugin.listeners;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.professions.event.GatheringEvent;
import com.runicrealms.plugin.professions.gathering.GatheringResource;
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
        Bukkit.broadcastMessage("achievement progress for cod is now: " + achievementStatus.getProgress());
    }
}
