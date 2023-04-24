package com.runicrealms.plugin;

import com.runicrealms.libs.acf.PaperCommandManager;
import com.runicrealms.libs.taskchain.BukkitTaskChainFactory;
import com.runicrealms.libs.taskchain.TaskChain;
import com.runicrealms.libs.taskchain.TaskChainFactory;
import com.runicrealms.plugin.api.AchievementsAPI;
import com.runicrealms.plugin.api.command.admin.ResetAchievementsCMD;
import com.runicrealms.plugin.listener.*;
import com.runicrealms.plugin.model.AchievementManager;
import com.runicrealms.plugin.model.MongoTask;
import com.runicrealms.plugin.shop.FastTravelShopInitializer;
import com.runicrealms.plugin.ui.AchievementUIListener;
import com.runicrealms.plugin.ui.PlayerMenuListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RunicAchievements extends JavaPlugin implements Listener {
    private static RunicAchievements plugin;
    private static TaskChainFactory taskChainFactory;
    private static AchievementsAPI api;
    private static PaperCommandManager commandManager;
    private static MongoTask mongoTask;

    public static RunicAchievements getInstance() {
        return plugin;
    }

    public static AchievementsAPI getAPI() {
        return api;
    }


    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }


    public static <T> TaskChain<T> newSharedChain(String name) {
        return taskChainFactory.newSharedChain(name);
    }

    public static PaperCommandManager getCommandManager() {
        return commandManager;
    }

    public static MongoTask getMongoTask() {
        return mongoTask;
    }

    @Override
    public void onDisable() {
        plugin = null;
        api = null;
        commandManager = null;
        mongoTask = null;
        taskChainFactory = null;
    }

    @Override
    public void onEnable() {
        plugin = this;
        taskChainFactory = BukkitTaskChainFactory.create(this);
        api = new AchievementManager();
        commandManager = new PaperCommandManager(this);
        mongoTask = new MongoTask();
        /*
        Shops
         */
        new FastTravelShopInitializer();
        registerCommands();
        registerEvents();
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
                        new AchievementUIListener(),
                        new PlayerMenuListener(),
                        new NoneSetListener(),
                        new ExplorerSetListener()
                );
    }

    private void registerEvents(Listener... listeners) {
        for (Listener listener : listeners) {
            this.getServer().getPluginManager().registerEvents(listener, this);
        }
    }
}
