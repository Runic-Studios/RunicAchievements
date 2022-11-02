package com.runicrealms.plugin.listener;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;
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
public class SlayerSetListener implements Listener {

    @EventHandler
    public void onSlay(MythicMobDeathEvent event) {

        if (!(event.getKiller() instanceof Player)) return;
        String internalName = event.getMobType().getInternalName();
        Achievement achievement = Achievement.getFromInternalMobName(internalName);
        if (achievement == null) return; // Ensure some achievement listens for this mob

        UUID uuid = event.getKiller().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return; // Ignore completed achievements

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + 1);
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            achievementStatus.setUnlocked(true);
            AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent((Player) event.getKiller(), achievementStatus.getAchievement());
            Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
        }
    }
}
