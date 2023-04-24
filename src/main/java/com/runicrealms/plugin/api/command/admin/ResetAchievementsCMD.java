package com.runicrealms.plugin.api.command.admin;

import com.runicrealms.libs.acf.BaseCommand;
import com.runicrealms.libs.acf.annotation.*;
import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.AchievementStatus;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.model.AchievementData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;

import java.util.UUID;

@CommandAlias("resetachievements")
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
        try (Jedis jedis = RunicCore.getRedisAPI().getNewJedisResource()) {
            UUID uuid = player.getUniqueId();
            AchievementData achievementData = (AchievementData) RunicAchievements.getAPI().getSessionData(uuid);
            for (String achievementId : achievementData.getAchievementStatusMap().keySet()) {
                Achievement achievement = Achievement.getFromId(achievementId);
                achievementData.getAchievementStatusMap().put(achievementId, new AchievementStatus(uuid, achievement));
            }
            achievementData.writeToJedis(jedis);
            player.sendMessage(ChatColor.RED + "Your achievements have been reset by an admin!");
        }
    }
}
