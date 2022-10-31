package com.runicrealms.plugin.listener;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.professions.event.GatheringEvent;
import com.runicrealms.plugin.professions.gathering.GatheringResource;
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

    // todo: generify
    @EventHandler
    public void onGather(GatheringEvent event) {
        if (event.getGatheringResource() != GatheringResource.COD) return;
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        if (achievementData.getAchievementStatusList().get(Achievement.FISH_10_COD.getId()).isUnlocked()) return;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(Achievement.FISH_10_COD.getId());
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + 1);
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        // if the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            achievementStatus.setUnlocked(true);
            AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(event.getPlayer(), achievementStatus.getAchievement());
            Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
        }
    }
}
