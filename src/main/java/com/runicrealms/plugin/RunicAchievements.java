package com.runicrealms.plugin;

import com.runicrealms.plugin.gui.AchievementGUIListener;
import com.runicrealms.plugin.gui.PlayerMenuListener;
import com.runicrealms.plugin.listeners.AchievementUnlockListener;
import com.runicrealms.plugin.listeners.FishingAchievementListener;
import com.runicrealms.plugin.model.AchievementManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RunicAchievements extends JavaPlugin implements Listener {

    private static RunicAchievements plugin;
    private static AchievementManager achievementManager;

    public static RunicAchievements getInstance() {
        return plugin;
    }

    public static AchievementManager getAchievementManager() {
        return achievementManager;
    }

    @Override
    public void onEnable() {
        plugin = this;
        achievementManager = new AchievementManager();
        registerEvents();
    }

    @Override
    public void onDisable() {
        plugin = null;
        achievementManager = null;
        registerEvents();
    }

    private void registerEvents() {
        this.registerEvents
                (
                        this,
                        new FishingAchievementListener(),
                        new AchievementUnlockListener(),
                        new AchievementGUIListener(),
                        new PlayerMenuListener()
                );
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
