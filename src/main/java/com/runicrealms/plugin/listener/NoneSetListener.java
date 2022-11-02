package com.runicrealms.plugin.listener;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.events.MagicDamageEvent;
import com.runicrealms.plugin.events.PhysicalDamageEvent;
import com.runicrealms.plugin.events.SpellCastEvent;
import com.runicrealms.plugin.events.SpellHealEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import com.runicrealms.runicguilds.api.event.GuildCreationEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import java.util.UUID;

public class NoneSetListener implements Listener {

    @EventHandler
    public void onLevelChange(PlayerLevelChangeEvent event) {
        if (event.getNewLevel() != 60) return;
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        Achievement achievement = Achievement.SO_IT_BEGINS;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;
        unlockAchievement(event.getPlayer(), achievementStatus);
    }

    @EventHandler
    public void onGuildCreate(GuildCreationEvent event) {
        UUID uuid = event.getGuild().getOwner().getUUID();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        Achievement achievement = Achievement.GUILDMASTER;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;
        unlockAchievement(Bukkit.getPlayer(event.getGuild().getOwner().getUUID()), achievementStatus);
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onHeal(SpellHealEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        Achievement achievement = Achievement.THE_SILENT_CARRY;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + event.getAmount());
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onMagicDamage(MagicDamageEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        Achievement achievement = Achievement.MAGIC_DAMAGE;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + event.getAmount());
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onPhysicalDamage(PhysicalDamageEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
        Achievement achievement = Achievement.PHYSICAL_DAMAGE;
        AchievementStatus achievementStatus = achievementData.getAchievementStatusMap().get(achievement.getId());
        if (achievementStatus.isUnlocked()) return;

        // Update the progress
        int progress = achievementStatus.getProgress();
        achievementStatus.setProgress(progress + event.getAmount());
        ProgressUnlock progressUnlock = (ProgressUnlock) achievementStatus.getAchievement().getUnlockMethod();

        // If the player has unlocked achievement
        if (achievementStatus.getProgress() == progressUnlock.getAmountToUnlock()) {
            unlockAchievement(event.getPlayer(), achievementStatus);
        }
    }


    @EventHandler(priority = EventPriority.HIGHEST) // last
    public void onSpellCast(SpellCastEvent event) {
        UUID uuid = event.getCaster().getUniqueId();
        AchievementData achievementData = RunicAchievements.getAchievementManager().loadAchievementData(uuid);
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
