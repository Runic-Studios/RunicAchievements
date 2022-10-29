package com.runicrealms.plugin.listeners;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementSet;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.RunicCoreAPI;
import com.runicrealms.plugin.events.AchievementUnlockEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.unlocks.LocationUnlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

/**
 * Used to handle achievements based on reaching a location.
 * Uses a running async task instead of PlayerMoveEvent to conserve resources
 *
 * @author Skyfallin
 */
public class ExplorerSetManager {

    public ExplorerSetManager() {
        Bukkit.getScheduler().runTaskTimerAsynchronously
                (
                        RunicAchievements.getInstance(),
                        this::checkForLocationAchievements,
                        0L,
                        20L
                );
    }

    private void checkForLocationAchievements() {
        for (UUID loaded : RunicCoreAPI.getLoadedCharacters()) {
            Player player = Bukkit.getPlayer(loaded);
            if (player == null) continue;
            checkForAchievementUnlock(player);
        }
    }

    /**
     * Checks all regions the current player is standing in.
     * If the region ID matches one for an achievement,
     *
     * @param player to check
     */
    private void checkForAchievementUnlock(Player player) {
        List<String> regionIds = RunicCoreAPI.getRegionIds(player.getLocation());
        if (regionIds.isEmpty()) return;
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(player.getUniqueId());
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getAchievementSet() != AchievementSet.EXPLORER) continue;
            if (!(achievement.getUnlockMethod() instanceof LocationUnlock)) continue;
            if (achievementData.getAchievementStatusList().get(achievement.getId()) == null) continue;
            if (achievementData.getAchievementStatusList().get(achievement.getId()).isUnlocked()) continue;
            AchievementStatus achievementStatus = achievementData.getAchievementStatusList().get(achievement.getId());
            String regionId = ((LocationUnlock) achievement.getUnlockMethod()).getRegionId();
            if (regionIds.contains(regionId)) {
                Bukkit.getScheduler().runTask(RunicAchievements.getInstance(), () -> triggerUnlockSynchronously(achievementStatus, player));
                return;
            }
        }
    }

    /**
     * Events cannot be triggered async, so we run this task later sync
     *
     * @param achievementStatus the status of the achievement to unlock
     * @param player            to unlock achievement for
     */
    private void triggerUnlockSynchronously(AchievementStatus achievementStatus, Player player) {
        achievementStatus.setUnlocked(true);
        AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(player, achievementStatus.getAchievement());
        Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
    }
}
