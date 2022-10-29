package com.runicrealms.plugin.rewards;

public class ExpReward extends Reward {

    private final int exp;

    public ExpReward(int exp) {
        super(RewardType.EXP);
        this.exp = exp;
        this.setRewardMessage(exp + " exp");
    }

    public int getExp() {
        return exp;
    }
}
