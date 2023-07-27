package com.runicrealms.plugin.achievements.api.event;

import com.runicrealms.plugin.achievements.Achievement;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This custom event is called when a player unlocks an achievement
 */
public class AchievementUnlockEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Achievement achievement;
    private boolean isCancelled;

    /**
     * Create a custom event to listen to
     *
     * @param player      who unlocked achievement
     * @param achievement that was unlocked
     */
    public AchievementUnlockEvent(Player player, Achievement achievement) {
        this.player = player;
        this.achievement = achievement;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean arg0) {
        this.isCancelled = arg0;
    }
}
