package com.runicrealms.plugin.achievements.reward;

import com.runicrealms.plugin.achievements.api.Reward;
import com.runicrealms.plugin.runicitems.RunicItemsAPI;
import com.runicrealms.plugin.runicitems.item.RunicItem;
import org.bukkit.Bukkit;

public class ItemReward implements Reward {

    private final String runicItemId;
    private final int amount;

    public ItemReward(String runicItemId, int amount) {
        this.runicItemId = runicItemId;
        this.amount = amount;
    }

    public String getRunicItemId() {
        return runicItemId;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String getRewardMessage() {
        try {
            RunicItem runicItem = RunicItemsAPI.generateItemFromTemplate(runicItemId);
            return runicItem.getDisplayableItem().getDisplayName() + " [x" + amount + "]";
        } catch (Exception ex) {
            Bukkit.getLogger().warning("[ERROR]: There was a problem loading an item reward for an achievement!");
            ex.printStackTrace();
        }
        return "";
    }
}
