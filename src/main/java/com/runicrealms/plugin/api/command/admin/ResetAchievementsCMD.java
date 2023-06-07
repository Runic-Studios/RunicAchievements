package com.runicrealms.plugin.api.command.admin;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Syntax;
import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.rdb.RunicDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@CommandAlias("resetachievements")
@CommandPermission("runic.op")
public class ResetAchievementsCMD extends BaseCommand {

    // resetachievements [player]

    @Default
    @CatchUnknown
    @Syntax("<player>")
    @CommandCompletion("@players")
    public static void onCommandReset(Player commandSender, @Default("Unknown User") String targetName) {
        Player player = Bukkit.getPlayer(targetName);
        if (player == null) {
            player = commandSender;
        }
        try (Jedis jedis = RunicDatabase.getAPI().getRedisAPI().getNewJedisResource()) {
            UUID uuid = player.getUniqueId();
            AchievementData achievementData = (AchievementData) RunicAchievements.getDataAPI().getSessionData(uuid);
            for (String achievementId : achievementData.getAchievementStatusMap().keySet()) {
                Achievement achievement = Achievement.getFromId(achievementId);
                achievementData.getAchievementStatusMap().put(achievementId, new AchievementStatus(uuid, achievement));
            }
            achievementData.writeToJedis(jedis);
            player.sendMessage(ChatColor.RED + "Your achievements have been reset by an admin!");
        }
    }
}
