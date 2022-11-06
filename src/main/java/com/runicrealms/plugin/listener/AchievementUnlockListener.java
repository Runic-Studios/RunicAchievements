package com.runicrealms.plugin.listener;

import com.runicrealms.plugin.Achievement;
import com.runicrealms.plugin.RunicAchievements;
import com.runicrealms.plugin.api.Reward;
import com.runicrealms.plugin.api.RunicCoreAPI;
import com.runicrealms.plugin.api.event.AchievementUnlockEvent;
import com.runicrealms.plugin.events.RunicExpEvent;
import com.runicrealms.plugin.model.AchievementData;
import com.runicrealms.plugin.reward.ExpReward;
import com.runicrealms.plugin.reward.ItemReward;
import com.runicrealms.plugin.utilities.ChatUtils;
import com.runicrealms.runicitems.RunicItemsAPI;
import com.runicrealms.runicitems.item.RunicItem;
import org.bukkit.Bukkit;
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
import redis.clients.jedis.Jedis;

public class AchievementUnlockListener implements Listener {

    /**
     * Shoots a green firework for more significant achievements
     *
     * @param player to shoot firework for
     * @param color  of the firework
     */
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
        if (achievement.shouldShootFirework())
            launchFirework(player, Color.AQUA);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 2.0f);
        ChatUtils.sendCenteredMessage(player, "");
        ChatUtils.sendCenteredMessage(player, "&2&lACHIEVEMENT COMPLETE: &e&l" + achievement.getName());
        for (Reward reward : achievement.getRewards()) {
            ChatUtils.sendCenteredMessage(player, "&aYou have earned: " + reward.getRewardMessage());
        }
        ChatUtils.sendCenteredMessage(player, "");
        for (Reward reward : achievement.getRewards()) {
            if (reward instanceof ExpReward) {
                handleExpReward(player, ((ExpReward) reward).getExp());
            } else if (reward instanceof ItemReward) {
                handleItemReward(player, (ItemReward) reward);
            } else {
                handleTitleReward(player);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST) // early
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework)
            event.setCancelled(true);
    }

    /**
     * Handles logic for giving player experience
     *
     * @param player to give exp to
     * @param exp    the amount of exp
     */
    public void handleExpReward(Player player, int exp) {
        RunicExpEvent runicExpEvent = new RunicExpEvent(exp, exp, player, RunicExpEvent.RunicExpSource.OTHER, 0, null);
        Bukkit.getPluginManager().callEvent(runicExpEvent);
    }

    /**
     * Handles logic for giving player item rewards
     *
     * @param player     to give item to
     * @param itemReward the item to give
     */
    private void handleItemReward(Player player, ItemReward itemReward) {
        String runicItemId = itemReward.getRunicItemId();
        RunicItem runicItem = RunicItemsAPI.generateItemFromTemplate(runicItemId, itemReward.getAmount());
        RunicItemsAPI.addItem(player.getInventory(), runicItem.generateItem());
    }

    /**
     * Handles logic for giving player title rewards. Does an immediate jedis write
     *
     * @param player to award title to
     */
    private void handleTitleReward(Player player) {
        try (Jedis jedis = RunicCoreAPI.getNewJedisResource()) {
            AchievementData achievementData = (AchievementData) RunicAchievements.getAchievementManager().loadSessionData(player.getUniqueId());
            achievementData.writeToJedis(jedis);
        }
    }
}
