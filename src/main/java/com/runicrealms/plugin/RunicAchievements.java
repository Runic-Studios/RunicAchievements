package com.runicrealms.plugin;

import co.aikar.commands.PaperCommandManager;
import com.runicrealms.plugin.api.command.admin.ResetAchievementsCMD;
import com.runicrealms.plugin.gui.AchievementGUIListener;
import com.runicrealms.plugin.gui.PlayerMenuListener;
import com.runicrealms.plugin.listener.*;
import com.runicrealms.plugin.model.AchievementManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RunicAchievements extends JavaPlugin implements Listener {

    private static RunicAchievements plugin;
    private static AchievementManager achievementManager;
    private static ExplorerSetManager explorerSetManager;
    private static PaperCommandManager commandManager;

    public static RunicAchievements getInstance() {
        return plugin;
    }

    public static AchievementManager getAchievementManager() {
        return achievementManager;
    }

    public static ExplorerSetManager getExplorerSetManager() {
        return explorerSetManager;
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public void onEnable() {
        plugin = this;
        achievementManager = new AchievementManager();
        explorerSetManager = new ExplorerSetManager();
        commandManager = new PaperCommandManager(this);
        registerCommands();
        registerEvents();
    }

    @Override
    public void onDisable() {
        plugin = null;
        achievementManager = null;
        explorerSetManager = null;
        commandManager = null;
    }

    private void registerCommands() {
        commandManager.registerCommand(new ResetAchievementsCMD());
    }

    private void registerEvents() {
        this.registerEvents
                (
                        this,
                        new GathererSetListener(),
                        new SlayerSetListener(),
                        new AchievementUnlockListener(),
                        new AchievementGUIListener(),
                        new PlayerMenuListener(),
                        new NoneSetListener()
                );
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
