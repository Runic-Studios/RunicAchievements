package com.runicrealms.plugin.listeners;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.events.AchievementUnlockEvent;
import com.runicrealms.plugin.rewards.Reward;
import com.runicrealms.plugin.utilities.ChatUtils;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class AchievementUnlockListener implements Listener {

    public static void launchFirework(Player player, Color color) {
        Firework firework = player.getWorld().spawn(player.getEyeLocation(), Firework.class);
        FireworkMeta meta = firework.getFireworkMeta();
        meta.setPower(0);
        meta.addEffect(FireworkEffect.builder().with(FireworkEffect.Type.BALL_LARGE).withColor(color).build());
        firework.setFireworkMeta(meta);
    }

    @EventHandler
    public void onAchievementUnlock(AchievementUnlockEvent event) {
        Player player = event.getPlayer();
        Achievement achievement = event.getAchievement();
        launchFirework(player, Color.GREEN);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2.0f);
        ChatUtils.sendCenteredMessage(player, "");
        ChatUtils.sendCenteredMessage(player, "&2&lACHIEVEMENT UNLOCKED: &e&l" + achievement.getName());
        for (Reward reward : achievement.getRewards()) {
            ChatUtils.sendCenteredMessage(player, "&aYou have earned: " + reward.getRewardMessage());
        }
        ChatUtils.sendCenteredMessage(player, "");
    }

    @EventHandler(priority = EventPriority.LOWEST) // early
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework)
            event.setCancelled(true);
    }
}
