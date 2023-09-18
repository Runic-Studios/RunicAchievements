package com.runicrealms.plugin.achievements.listener;

import com.runicrealms.plugin.achievements.Achievement;
import com.runicrealms.plugin.achievements.AchievementStatus;
import com.runicrealms.plugin.achievements.RunicAchievements;
import com.runicrealms.plugin.achievements.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.achievements.model.AchievementData;
import com.runicrealms.plugin.achievements.unlock.ProgressUnlock;
import com.runicrealms.plugin.events.MagicDamageEvent;
import com.runicrealms.plugin.events.PhysicalDamageEvent;
import com.runicrealms.plugin.events.SpellCastEvent;
import com.runicrealms.plugin.events.SpellHealEvent;
import com.runicrealms.plugin.runicguilds.api.event.GuildCreationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.UUID;

public class NoneSetListener implements Listener {

    @EventHandler
    public void onGuildCreate(GuildCreationEvent event) {
        UUID uuid = event.getUuid();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.GUILDMASTER;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;
        unlockAchievement(Bukkit.getPlayer(event.getUuid()), achievementStatus);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onHeal(SpellHealEvent event) {
        if (event.getSpell() == null) return; // Ignore potions, duel healing, etc.
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.THE_SILENT_CARRY;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(Math.min(progress + event.getAmount(), progressUnlock.getAmountToUnlock()));

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() >= progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {
        if (event.getOldLevel() == 0) return; // max level players logging in
        if (event.getNewLevel() != 60) return;
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.SO_IT_BEGINS;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;
        unlockAchievement(event.getPlayer(), achievementStatus);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onMagicDamage(MagicDamageEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.MAGIC_DAMAGE;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        achievementStatus.setProgress(Math.min(progress + event.getAmount(), progressUnlock.getAmountToUnlock()));

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() >= progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onPhysicalDamage(PhysicalDamageEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.PHYSICAL_DAMAGE;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();
        achievementStatus.setProgress(Math.min(progress + event.getAmount(), progressUnlock.getAmountToUnlock()));

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() >= progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onSpellCast(SpellCastEvent event) {
        UUID uuid = event.getCaster().getUniqueId();
        AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
        Achievement achievement = Achievement.CAST_SPELLS;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + 1);
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getCaster(), achievementStatus);
        }
    }

    /**
     * Unlock achievement for player and call even for relevant listeners
     *
     * @param player            who unlocked
     * @param achievementStatus wrapper for achievement containing progress
     */
    private void unlockAchievement(Player player, AchievementStatus achievementStatus) {
        achievementStatus.setUnlocked(true);
        AchievementUnlockEvent achievementUnlockEvent = new AchievementUnlockEvent(player, achievementStatus.getAchievement());
        Bukkit.getPluginManager().callEvent(achievementUnlockEvent);
    }
}
