package com.runicrealms.plugin.rewards;

import com.runicrealms.runicitems.RunicItemsAPI;
import com.runicrealms.runicitems.item.RunicItem;
import org.bukkit.Bukkit;

public class ItemReward extends Reward {

    private final String runicItemId;
    private final int amount;

    public ItemReward(String runicItemId, int amount) {
        super(RewardType.ITEM);
        this.runicItemId = runicItemId;
        this.amount = amount;
        try {
            RunicItem runicItem = RunicItemsAPI.generateItemFromTemplate(runicItemId);
            this.setRewardMessage(runicItem.getDisplayableItem().getDisplayName() + " [x" + amount + "]");
        } catch (Exception ex) {
            Bukkit.getLogger().warning("[ERROR]: There was a problem loading an item reward for an achievement!");
            ex.printStackTrace();
        }
    }

    public String getRunicItemId() {
        return runicItemId;
    }

    public int getAmount() {
        return amount;
    }
}
