package com.runicrealms.plugin;

import com.runicrealms.plugin.gui.AchievementGUIListener;
import com.runicrealms.plugin.gui.PlayerMenuListener;
import com.runicrealms.plugin.listener.AchievementUnlockListener;
import com.runicrealms.plugin.listener.ExplorerSetManager;
import com.runicrealms.plugin.listener.GathererSetListener;
import com.runicrealms.plugin.model.AchievementManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RunicAchievements extends JavaPlugin implements Listener {

    private static RunicAchievements plugin;
    private static AchievementManager achievementManager;
    private static ExplorerSetManager explorerSetManager;

    public static RunicAchievements getInstance() {
        return plugin;
    }

    public static AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public static ExplorerSetManager getExplorerSetManager() {
        return explorerSetManager;
    }

    @Override
    public void onEnable() {
        plugin = this;
        achievementManager = new AchievementManager();
        explorerSetManager = new ExplorerSetManager();
        registerEvents();
    }

    @Override
    public void onDisable() {
        plugin = null;
        achievementManager = null;
        explorerSetManager = null;
    }

    private void registerEvents() {
        this.registerEvents
                (
                        this,
                        new GathererSetListener(),
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
