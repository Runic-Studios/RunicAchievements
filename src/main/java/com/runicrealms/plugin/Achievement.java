package com.runicrealms.plugin;

import com.runicrealms.plugin.rewards.ItemReward;
import com.runicrealms.plugin.rewards.Reward;
import com.runicrealms.plugin.unlocks.ProgressUnlock;
import com.runicrealms.plugin.unlocks.UnlockMethod;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public enum Achievement {

    FISH_10_COD
            (
                    "fish-10-cod",
                    "Fish 10 Cod",
                    "Obtained by fishing 10 cod!",
                    5,
                    Material.COD,
                    new ProgressUnlock(2),
                    Collections.singletonList(new ItemReward("OakWood", 10)),
                    AchievementSet.MASTER_GATHERER
            );

    private final String id;
    private final String name;
    private final String description;
    private final int pointValue;
    private final Material guiItem;
    private final UnlockMethod unlockMethod;
    private final List<Reward> rewards;
    private final AchievementSet achievementSet;

    Achievement(String id, String name, String description, int pointValue, Material guiItem,
                UnlockMethod unlockMethod, List<Reward> rewards, AchievementSet achievementSet) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        this.guiItem = guiItem;
        this.unlockMethod = unlockMethod;
        this.rewards = rewards;
        this.achievementSet = achievementSet;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPointValue() {
        return pointValue;
    }

    public Material getGuiItem() {
        return guiItem;
    }

    public UnlockMethod getUnlockMethod() {
        return unlockMethod;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public AchievementSet getAchievementSet() {
        return achievementSet;
    }
}
