package com.runicrealms.plugin;

import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.api.UnlockMethod;
import com.runicrealms.plugin.reward.TitleReward;
import com.runicrealms.plugin.unlock.LocationUnlock;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

/**
 * IMPORTANT: only use titles for patch 2.0! We need character-specific achievements moving forward.
 */
public enum Achievement {

    GO_FISH
            (
                    "go-fish",
                    "Go Fish!",
                    "Obtained by fishing 1 cod!",
                    25,
                    Material.COD,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("Fishing Monarch", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Cod"
            ),
    GATHER_WHEAT
            (
                    "gather-wheat",
                    "It Ain't Much",
                    "Obtained by farming 1 wheat!",
                    25,
                    Material.WHEAT,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("Humble Farmer", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Wheat"
            ),
    DISCOVER_KOLDORE
            (
                    "discover-koldore",
                    "Digging Deeper",
                    "Obtained by discovering Koldore!",
                    5,
                    Material.IRON_PICKAXE,
                    new LocationUnlock("koldore"),
                    Collections.singletonList(new TitleReward("Koldorian", false)),
                    AchievementSet.EXPLORER,
                    false,
                    ""
            );

    private final String id;
    private final String name;
    private final String description;
    private final int pointValue;
    private final Material guiItem;
    private final UnlockMethod unlockMethod;
    private final List<Reward> rewards;
    private final AchievementSet achievementSet;
    private final boolean shootsFirework;
    private final String runicItemId;

    /**
     * An enumerated list of achievements
     *
     * @param id             of the achievement. MUST be unique. used for data storage
     * @param name           of the achievement. for ui displays, chat
     * @param description    of the achievement
     * @param pointValue     the achievement points
     * @param guiItem        the material of the achievement in the ui
     * @param unlockMethod   how to unlock the achievement
     * @param rewards        a list of rewards
     * @param achievementSet some achievements belong to a greater 'set'
     * @param shootsFirework whether the achievement will shoot a firework
     * @param runicItemId    an optional param to add a runic item for gathering achievements
     */
    Achievement(String id, String name, String description, int pointValue, Material guiItem,
                UnlockMethod unlockMethod, List<Reward> rewards, AchievementSet achievementSet,
                boolean shootsFirework, String runicItemId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pointValue = pointValue;
        this.guiItem = guiItem;
        this.unlockMethod = unlockMethod;
        this.rewards = rewards;
        this.achievementSet = achievementSet;
        this.shootsFirework = shootsFirework;
        this.runicItemId = runicItemId;
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

    public boolean shouldShootFirework() {
        return shootsFirework;
    }

    /**
     * Returns an achievement with an id matching parameter
     *
     * @param achievementId of the achievement
     * @return the achievement or null if the id is not found
     */
    public static Achievement getFromId(String achievementId) {
        try {
            for (Achievement achievement : Achievement.values()) {
                if (achievement.getId().equalsIgnoreCase(achievementId)) {
                    return achievement;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Returns an achievement with a material matching parameter
     *
     * @param runicItemId of the item to gather
     * @return the achievement or null if the runic item id is not found
     */
    public static Achievement getFromRunicItemId(String runicItemId) {
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getRunicItemId().equals(runicItemId)) {
                return achievement;
            }
        }
        return null;
    }

    /**
     * Returns an achievement with a material matching parameter
     *
     * @param material of the achievement icon
     * @return the achievement or null if the material is not found
     */
    public static Achievement getFromMaterial(Material material) {
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getGuiItem() == material) {
                return achievement;
            }
        }
        return null;
    }

    public String getRunicItemId() {
        return runicItemId;
    }
}
