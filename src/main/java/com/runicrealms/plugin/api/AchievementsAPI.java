package com.runicrealms.plugin.api;

import com.runicrealms.plugin.rdb.model.SessionDataNested;
import org.bukkit.entity.Player;

import java.util.Map;

/**
 * Used to selectively expose functionality of plugin and SessionDataNestedManager
 * NOT redundant
 *
 * @author Skyfallin
 */
public interface AchievementsAPI {

    /**
     * Tries to retrieve an AchievementData object from server memory.
     * Returns null if data is not cached in-memory!
     *
     * @param object uuid of the player
     * @return an AchievementData object
     */
    SessionDataNested getSessionData(Object object, int... slot);

    /**
     * Check whether the player has unlocked the given achievement
     *
     * @param player        to check
     * @param achievementId unique internal id of the achievement
     * @return true if the player has unlocked it
     */
    boolean hasAchievement(Player player, String achievementId);

    /**
     * @return The map of in-memory achievement data
     */
    Map<Object, SessionDataNested> getSessionDataMap();

    /**
     * Creates an AchievementData object. Tries to build it from session storage (Redis) first,
     * then falls back to Mongo
     *
     * @param object uuid of player who is attempting to load their data
     */
    SessionDataNested loadSessionData(Object object, int... slot);

}
