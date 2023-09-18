package com.runicrealms.plugin.achievements;

import co.aikar.commands.PaperCommandManager;
import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import com.runicrealms.plugin.achievements.api.AchievementsDataAPI;
import com.runicrealms.plugin.achievements.api.command.admin.ResetAchievementsCMD;
import com.runicrealms.plugin.achievements.leaderboard.LeaderboardManager;
import com.runicrealms.plugin.achievements.listener.AchievementUnlockListener;
import com.runicrealms.plugin.achievements.listener.ExplorerSetListener;
import com.runicrealms.plugin.achievements.listener.GathererSetListener;
import com.runicrealms.plugin.achievements.listener.NoneSetListener;
import com.runicrealms.plugin.achievements.listener.SlayerSetListener;
import com.runicrealms.plugin.achievements.model.AchievementManager;
import com.runicrealms.plugin.achievements.model.MongoTask;
import com.runicrealms.plugin.achievements.shop.FastTravelShopInitializer;
import com.runicrealms.plugin.achievements.ui.AchievementProfileUIListener;
import com.runicrealms.plugin.achievements.ui.AchievementUIListener;
import com.runicrealms.plugin.common.RunicCommon;
import com.runicrealms.plugin.common.api.AchievementsAPI;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class RunicAchievements extends JavaPlugin implements Listener {
    private static RunicAchievements plugin;
    private static TaskChainFactory taskChainFactory;
    private static AchievementsDataAPI dataAPI;
    private static PaperCommandManager commandManager;
    private static MongoTask mongoTask;

    public static RunicAchievements getInstance() {
        return plugin;
    }

    public static AchievementsDataAPI getDataAPI() {
        return dataAPI;
    }

    public static AchievementsAPI getAchievementsAPI() {
        return RunicCommon.getAchievementsAPI();
    }

    public static <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
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
        dataAPI = null;
        commandManager = null;
        mongoTask = null;
        taskChainFactory = null;
    }

    @Override
    public void onEnable() {
        plugin = this;
        taskChainFactory = BukkitTaskChainFactory.create(this);

        AchievementManager apiImplementation = new AchievementManager();
        dataAPI = apiImplementation;
        RunicCommon.registerAchievementsAPI(apiImplementation);

        commandManager = new PaperCommandManager(this);
        mongoTask = new MongoTask();
        new LeaderboardManager(); // todo
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
                        new AchievementProfileUIListener(),
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
