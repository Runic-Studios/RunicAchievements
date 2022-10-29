package com.runicrealms.plugin.reward;

import com.runicrealms.plugin.api.Reward;

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
