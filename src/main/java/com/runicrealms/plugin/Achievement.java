package com.runicrealms.plugin;

import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.api.UnlockMethod;
import com.runicrealms.plugin.reward.TitleReward;
import com.runicrealms.plugin.unlock.LocationUnlock;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import com.runicrealms.plugin.unlock.TriggerUnlock;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

/**
 * IMPORTANT: only use titles for patch 2.0! We need character-specific achievements moving forward.
 */
public enum Achievement {

    /*
    None set
     */
    SO_IT_BEGINS
            (
                    "so-it-begins",
                    "And So It Begins",
                    "Obtained by reaching level 60!",
                    25,
                    Material.EXPERIENCE_BOTTLE,
                    new TriggerUnlock(),
                    Collections.singletonList(new TitleReward("Neckbeard", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    ""
            ),
    GUILDMASTER
            (
                    "guildmaster",
                    "Making Your Mark",
                    "Obtained by creating a guild!",
                    25,
                    Material.IRON_HORSE_ARMOR,
                    new TriggerUnlock(),
                    Collections.singletonList(new TitleReward("Guildmaster", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    ""
            ),
    CAST_SPELLS
            (
                    "cast-spells",
                    "Runespeaker",
                    "Obtained by casting 500,000 spells!",
                    75,
                    Material.NETHER_WART,
                    new ProgressUnlock(500000),
                    Collections.singletonList(new TitleReward("Arcanist", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    ""
            ),
    THE_SILENT_CARRY
            (
                    "the-silent-carry",
                    "The Silent Carry",
                    "Obtained by restoring 5,000,000 total health!",
                    115,
                    Material.HEART_OF_THE_SEA,
                    new ProgressUnlock(5000000),
                    Collections.singletonList(new TitleReward("The Faithful", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    ""
            ),

    /*
    Master Gatherer Set
     */
    GATHER_WHEAT
            (
                    "gather-wheat",
                    "It Ain't Much",
                    "Obtained by farming 7,000 wheat!",
                    70,
                    Material.WHEAT,
                    new ProgressUnlock(7000),
                    Collections.singletonList(new TitleReward("Humble Farmer", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Wheat",
                    ""
            ),
    SMELL_ROSES
            (
                    "smell-roses",
                    "Stop and Smell the Roses",
                    "Obtained by harvesting 1,000 petunias!",
                    70,
                    Material.WHEAT,
                    new ProgressUnlock(1000),
                    Collections.singletonList(new TitleReward("Green Thumb", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Petunia",
                    ""
            ),
    // todo: NEEDS MULTIPLE MOBS. ADD ALL GOBLINS TO GOBLIN TOWN, ALL AZANA CITIZENS
    BOIL_EM_MASH_EM
            (
                    "boil-em-mash-em",
                    "Boil 'em Mash 'em",
                    "Obtained by farming 1,000 potatoes!",
                    35,
                    Material.POTATO,
                    new ProgressUnlock(1000),
                    Collections.singletonList(new TitleReward("Tater Tot", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Potato",
                    ""
            ),
    JUNGLE_LOG
            (
                    "jungle-log",
                    "George of the Jungle",
                    "Obtained by felling 5,000 jungle trees!",
                    60,
                    Material.JUNGLE_LOG,
                    new ProgressUnlock(5000),
                    Collections.singletonList(new TitleReward("Harambe", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "JungleWood",
                    ""
            ),
    GO_FISH
            (
                    "go-fish",
                    "Go Fish!",
                    "Obtained by fishing 10,000 cod!",
                    100,
                    Material.COD,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Fishing Regent", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Cod",
                    ""
            ),
    ARE_YOU_INSANE
            (
                    "are-you-insane",
                    "Are You Insane?",
                    "Obtained by fishing 10,000 pufferfish!",
                    200,
                    Material.PUFFERFISH,
                    new ProgressUnlock(5000),
                    Collections.singletonList(new TitleReward("Touch Grass", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Pufferfish",
                    ""
            ),
    THE_LUMBERJACK
            (
                    "the-lumberjack",
                    "Fires of Isengard",
                    "Obtained by felling 10,000 oak trees!",
                    100,
                    Material.OAK_LOG,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("The Lumberjack", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "OakWood",
                    ""
            ),

    /*
    Explorer set
     */
    DISCOVER_KOLDORE
            (
                    "discover-koldore",
                    "Digging Deeper",
                    "Obtained by discovering Koldore!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("koldore"),
                    Collections.singletonList(new TitleReward("Koldorian", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    DISCOVER_HILSTEAD
            (
                    "discover-hilstead",
                    "So Many Beards",
                    "Obtained by discovering Hilstead!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("hilstead"),
                    Collections.singletonList(new TitleReward("Viking", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    DISCOVER_MISTY_ALCOVE
            (
                    "discover-misty-alcove",
                    "The Black Market",
                    "Obtained by discovering the Misty Alcove!",
                    15,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("misty_alcove"),
                    Collections.singletonList(new TitleReward("?", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    // todo: finish misty alcove
    DISCOVER_WINTERVALE
            (
                    "discover-wintervale",
                    "The White Council",
                    "Obtained by discovering Wintervale!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("wintervale"),
                    Collections.singletonList(new TitleReward("White Mage", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    DISCOVER_ISFODAR
            (
                    "discover-isfodar",
                    "The Hidden Kingdom",
                    "Obtained by discovering Isfodar!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("isfodar"),
                    Collections.singletonList(new TitleReward("Elven", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    DISCOVER_ZENYTH
            (
                    "discover-zenyth",
                    "The Eastern Kingdom",
                    "Obtained by discovering Zenyth!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("zenyth"),
                    Collections.singletonList(new TitleReward("Sultan", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),
    DISCOVER_VALMYRA
            (
                    "discover-valmyra",
                    "Forged of Flame",
                    "Obtained by discovering Valmyra!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("valmyra"),
                    Collections.singletonList(new TitleReward("Flamebringer", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    ""
            ),

    /*
    Slayer set achievements
     */
    THE_SCARECROW
            (
                    "the-scarecrow",
                    "Only Had a Brain",
                    "Obtained by defeating the Scarecrow 100 times!",
                    25,
                    Material.BEE_SPAWN_EGG,
                    new ProgressUnlock(100),
                    Collections.singletonList(new TitleReward("The Scarecrow", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "Scarecrow"
            ),
    SLAY_GOBLINS
            (
                    "down-in-goblin-town",
                    "Down in Goblin Town!",
                    "Obtained by slaying 10,000 goblins!",
                    50,
                    Material.ZOMBIE_SPAWN_EGG,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("The Goblin Cleaver", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "azana_goblin"
            ),
    SLAY_SEBATH
            (
                    "slay-sebath",
                    "Silkwood Hero",
                    "Obtained by slaying the orc Sebath!",
                    25,
                    Material.STRIDER_SPAWN_EGG,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("The Headsman", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "sebath"
            ),
    ILL_IN_PAIN
            (
                    "ill-in-pain",
                    "Ill in Pain",
                    "Obtained by slaying 10,000 Azanian Citizen!",
                    5,
                    Material.VILLAGER_SPAWN_EGG,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Executioner", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "AzanaCitizen"
            ),
    GOT_MILK
            (
                    "got-milk",
                    "Got Milk?",
                    "Obtained by slaying the Cow Mother 15 times!",
                    25,
                    Material.COW_SPAWN_EGG,
                    new ProgressUnlock(15),
                    Collections.singletonList(new TitleReward("Carnivore", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "CowMother"
            ),
    SLAY_INFERNAL_GRUNT
            (
                    "slay-infernal-grunt",
                    "Forged of Fire",
                    "Obtained by slaying 1,000 Infernal Grunts!",
                    100,
                    Material.BLAZE_SPAWN_EGG,
                    new ProgressUnlock(1000),
                    Collections.singletonList(new TitleReward("Hellknight", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "InfernalGrunt"
            ),
    SLAY_ELDRID
            (
                    "slay-eldrid",
                    "The Frost Lords",
                    "Obtained by defeating The Frozen Fortress dungeon and slaying Eldrid the Betrayer!",
                    1000,
                    Material.DROWNED_SPAWN_EGG,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("The Betrayer", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    "Eldrid"
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
    private final String internalMobName;

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
                boolean shootsFirework, String runicItemId, String internalMobName) {
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
        this.internalMobName = internalMobName;
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
     * Returns an achievement with a material matching parameter
     *
     * @param internalMobName of the mob to slay
     * @return the achievement or null if the material is not found
     */
    public static Achievement getFromInternalMobName(String internalMobName) {
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getInternalMobName().equals(internalMobName)) {
                return achievement;
            }
        }
        return null;
    }

    public String getRunicItemId() {
        return runicItemId;
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

    public String getInternalMobName() {
        return internalMobName;
    }
}
