package com.runicrealms.plugin;

import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.api.UnlockMethod;
import com.runicrealms.plugin.reward.TitleReward;
import com.runicrealms.plugin.unlock.LocationUnlock;
import com.runicrealms.plugin.unlock.ProgressUnlock;
import com.runicrealms.plugin.unlock.TriggerUnlock;
import org.bukkit.Material;

import java.util.Arrays;
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
                    Collections.singletonList("")
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
                    Collections.singletonList("")
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
                    Collections.singletonList("")
            ),
    THE_SILENT_CARRY
            (
                    "the-silent-carry",
                    "The Silent Carry",
                    "Obtained by restoring✸ 5,000,000 total health!",
                    115,
                    Material.HEART_OF_THE_SEA,
                    new ProgressUnlock(5000000),
                    Collections.singletonList(new TitleReward("Faithful", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    Collections.singletonList("")
            ),
    MAGIC_DAMAGE
            (
                    "magic-damage",
                    "Abra Kadabra",
                    "Obtained by dealing 10,000,000 total magicʔ damage!",
                    115,
                    Material.LAPIS_LAZULI,
                    new ProgressUnlock(10000000),
                    Collections.singletonList(new TitleReward("Wizard", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    Collections.singletonList("")
            ),
    PHYSICAL_DAMAGE
            (
                    "physical-damage",
                    "Unsheathed Steel",
                    "Obtained by dealing 10,000,000 total physical⚔ damage!",
                    115,
                    Material.REDSTONE,
                    new ProgressUnlock(10000000),
                    Collections.singletonList(new TitleReward("Brute", false)),
                    AchievementSet.NONE,
                    true,
                    "",
                    Collections.singletonList("")
            ),

    /*
    Combatant Set
     */
    ARENA_MASTER
            (
                    "arena-master",
                    "Battle Tested",
                    "Obtained by winning 500 total arena matches!",
                    135,
                    Material.IRON_SWORD,
                    new ProgressUnlock(500),
                    Collections.singletonList(new TitleReward("Gladiator", false)),
                    AchievementSet.COMBATANT,
                    true,
                    "",
                    Collections.singletonList("")
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
                    "wheat",
                    Collections.singletonList("")
            ),
    SMELL_ROSES
            (
                    "smell-roses",
                    "Stop and Smell the Roses",
                    "Obtained by harvesting 10,000 plants!",
                    70,
                    Material.POPPY,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Green Thumb", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "*",
                    Collections.singletonList("")
            ),
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
                    "potato",
                    Collections.singletonList("")
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
                    "jungle-wood",
                    Collections.singletonList("")
            ),
    GO_FISH
            (
                    "go-fish",
                    "Go Fish!",
                    "Obtained by fishing 10,000 cod!",
                    100,
                    Material.COD,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Fishing Monarch", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "Cod",
                    Collections.singletonList("")
            ),
    ARE_YOU_INSANE
            (
                    "are-you-insane",
                    "Are You Insane?",
                    "Obtained by fishing 5,000 pufferfish!",
                    200,
                    Material.PUFFERFISH,
                    new ProgressUnlock(5000),
                    Collections.singletonList(new TitleReward("Touch Grass", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "pufferfish",
                    Collections.singletonList("")
            ),
    THE_LUMBERJACK
            (
                    "the-lumberjack",
                    "Fires of Isengard",
                    "Obtained by felling 10,000 oak trees!",
                    100,
                    Material.OAK_LOG,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Lumberjack", false)),
                    AchievementSet.MASTER_GATHERER,
                    true,
                    "oak-wood",
                    Collections.singletonList("")
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
                    Collections.singletonList("")
            ),
    DISCOVER_WHALETOWN
            (
                    "discover-whaletown",
                    "Land Lubber",
                    "Obtained by discovering Whaletown!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("whaletown"),
                    Collections.singletonList(new TitleReward("Sailor", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
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
                    Collections.singletonList("")
            ),
    DISCOVER_MISTY_ALCOVE
            (
                    "discover-misty-alcove",
                    "The Black Market",
                    "Obtained by discovering the Misty Alcove!",
                    15,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("misty_alcove"),
                    Collections.singletonList(new TitleReward("Merchant", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
            ),
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
                    Collections.singletonList("")
            ),
    DISCOVER_DEAD_MANS_REST
            (
                    "discover-dead-mans-rest",
                    "Ghost Town",
                    "Obtained by discovering Dead Man's Rest!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("dead_mans_rest"),
                    Collections.singletonList(new TitleReward("Vagabond", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
            ),
    DISCOVER_ISFODAR
            (
                    "discover-isfodar",
                    "Kingdom of the Elves",
                    "Obtained by discovering Isfodar!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("isfodar"),
                    Collections.singletonList(new TitleReward("Elven", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
            ),
    DISCOVER_TIRENEAS
            (
                    "discover-tireneas",
                    "Taking Refuge",
                    "Obtained by discovering Tireneas!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("tireneas"),
                    Collections.singletonList(new TitleReward("Traveler", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
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
                    Collections.singletonList("")
            ),
    DISCOVER_NAHEEN
            (
                    "discover-naheen",
                    "Diamond in the Rough",
                    "Obtained by discovering Naheen!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("naheen"),
                    Collections.singletonList(new TitleReward("Ruffian", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
            ),
    DISCOVER_NAZMORA
            (
                    "discover-nazmora",
                    "Lok'tar Ogar",
                    "Obtained by discovering Nazmora!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("nazmora"),
                    Collections.singletonList(new TitleReward("Orc", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
            ),
    DISCOVER_STONEHAVEN
            (
                    "discover-stonehaven",
                    "Heart of the Mountain",
                    "Obtained by discovering Stonehaven!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("stonehaven"),
                    Collections.singletonList(new TitleReward("Dwarven", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
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
                    Collections.singletonList("")
            ),
    DISCOVER_FROSTS_END
            (
                    "discover-frosts-end",
                    "The Frost Lords",
                    "Obtained by discovering Frost's End!",
                    5,
                    Material.WRITABLE_BOOK,
                    new LocationUnlock("frosts_end"),
                    Collections.singletonList(new TitleReward("Frost Lord", false)),
                    AchievementSet.EXPLORER,
                    false,
                    "",
                    Collections.singletonList("")
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
                    Collections.singletonList(new TitleReward("Scarecrow", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    Collections.singletonList("Scarecrow")
            ),
    SLAY_GOBLINS
            (
                    "down-in-goblin-town",
                    "Down in Goblin Town!",
                    "Obtained by slaying 10,000 goblins!",
                    50,
                    Material.ZOMBIE_SPAWN_EGG,
                    new ProgressUnlock(10000),
                    Collections.singletonList(new TitleReward("Goblin Cleaver", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    Arrays.asList
                            (
                                    "azana_goblin",
                                    "GoblinArcher",
                                    "GoblinCorpArcher",
                                    "GoblinCorpFootSoldier",
                                    "GoblinFootSoldier",
                                    "GoblinRider",
                                    "GoblinScout",
                                    "GoblinThief"
                            )
            ),
    SLAY_SEBATH
            (
                    "slay-sebath",
                    "Silkwood Hero",
                    "Obtained by slaying the orc Sebath!",
                    25,
                    Material.STRIDER_SPAWN_EGG,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("Headsman", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    Collections.singletonList("sebath")
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
                    Arrays.asList("AzanaCitizen", "AzanaCitizen2")
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
                    Collections.singletonList("CowMother")
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
                    Collections.singletonList("InfernalGrunt")
            ),
    SLAY_ELDRID
            (
                    "slay-eldrid",
                    "Realm's Champion",
                    "Coming soon!", // Obtained by defeating The Frozen Fortress dungeon and slaying Eldrid the Betrayer!
                    200,
                    Material.DROWNED_SPAWN_EGG,
                    new ProgressUnlock(1),
                    Collections.singletonList(new TitleReward("Betrayer", false)),
                    AchievementSet.SLAYER,
                    false,
                    "",
                    Collections.singletonList("Eldrid")
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
    private final List<String> internalMobNames;

    /**
     * An enumerated list of achievements
     *
     * @param id               of the achievement. MUST be unique. used for data storage
     * @param name             of the achievement. for ui displays, chat
     * @param description      of the achievement
     * @param pointValue       the achievement points
     * @param guiItem          the material of the achievement in the ui
     * @param unlockMethod     how to unlock the achievement
     * @param rewards          a list of rewards
     * @param achievementSet   some achievements belong to a greater 'set'
     * @param shootsFirework   whether the achievement will shoot a firework
     * @param runicItemId      a param to add a runic item for gathering achievements
     * @param internalMobNames a list of Strings of mobs to slay (if any)
     */
    Achievement(String id, String name, String description, int pointValue, Material guiItem,
                UnlockMethod unlockMethod, List<Reward> rewards, AchievementSet achievementSet,
                boolean shootsFirework, String runicItemId, List<String> internalMobNames) {
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
        this.internalMobNames = internalMobNames;
    }

    /**
     * Returns an achievement with a material matching parameter
     *
     * @param internalMobName of the mob to slay
     * @return the achievement or null if the material is not found
     */
    public static Achievement getFromInternalMobName(String internalMobName) {
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getInternalMobNames().contains(internalMobName)) {
                return achievement;
            }
        }
        return null;
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
            if (achievement.getRunicItemId().equalsIgnoreCase(runicItemId)) {
                return achievement;
            }
        }
        return null;
    }

    /**
     * Returns an achievement with a material matching parameter
     *
     * @param displayName of the achievement icon
     * @return the achievement or null if the material is not found
     */
    public static Achievement getFromDisplayName(String displayName) {
        for (Achievement achievement : Achievement.values()) {
            if (achievement.getName().equalsIgnoreCase(displayName)) {
                return achievement;
            }
        }
        return null;
    }

    public AchievementSet getAchievementSet() {
        return achievementSet;
    }

    public String getDescription() {
        return description;
    }

    public Material getGuiItem() {
        return guiItem;
    }

    public String getId() {
        return id;
    }

    public List<String> getInternalMobNames() {
        return internalMobNames;
    }

    public String getName() {
        return name;
    }

    public int getPointValue() {
        return pointValue;
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public String getRunicItemId() {
        return runicItemId;
    }

    public UnlockMethod getUnlockMethod() {
        return unlockMethod;
    }

    public boolean shouldShootFirework() {
        return shootsFirework;
    }
}
