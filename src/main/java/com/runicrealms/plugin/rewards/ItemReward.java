package com.runicrealms.plugin.rewards;

public class ItemReward extends Reward {

    private final String runicItemId;
    private final int amount;

    public ItemReward(String runicItemId, int amount) {
        super(RewardType.ITEM);
        this.runicItemId = runicItemId;
        this.amount = amount;
    }

    public String getRunicItemId() {
        return runicItemId;
    }

    public int getAmount() {
        return amount;
    }
}
