package com.runicrealms.plugin.achievements.listener;

import com.runicrealms.plugin.achievements.Achievement;
import com.runicrealms.plugin.achievements.AchievementSet;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.achievements.model.AchievementData;
import com.runicrealms.plugin.achievements.unlock.LocationUnlock;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Used to handle achievements based on reaching a location.
 *
 * @author Skyfallin
 */
public class ExplorerSetListener implements Listener {

    /**
     * Checks all regions the current player is standing in.
     * If the region ID matches one for an achievement,
     *
     * @param player to check
     */
    private void checkForAchievementUnlock(Player player, String regionName) {
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(player.getUniqueId());
        if (achievementData == null) return; // Player not fully loaded yet
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getAchievementSet() != AchievementSet.EXPLORER) continue;
            if (!(achievement.getUnlockMethod() instanceof LocationUnlock)) continue;
            if (achievementData.getAchievementStatusMap().get(achievement.getId()) == null)
                continue;
            if (achievementData.getAchievementStatusMap().get(achievement.getId()).isUnlocked())
                continue;
            AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
            String regionId = ((LocationUnlock) achievement.getUnlockMethod()).getRegionId();
            if (regionName.equalsIgnoreCase(regionId)) {
                Bukkit.getScheduler().runTask(RunicAchievements.getInstance(), () -> triggerUnlockSynchronously(achievementStatus, player));
                return;
            }
        }
    }

    @EventHandler
    public void onRegionEntered(RegionEnteredEvent event) {
        if (event.getPlayer() == null) return;
        checkForAchievementUnlock(event.getPlayer(), event.getRegionName());
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
