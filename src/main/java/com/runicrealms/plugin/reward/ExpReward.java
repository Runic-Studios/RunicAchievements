package com.runicrealms.plugin.reward;

import com.runicrealms.plugin.api.Reward;

public class ExpReward implements Reward {

    private final int exp;

    public ExpReward(int exp) {
        this.exp = exp;
    }

    public int getExp() {
        return exp;
    }

    @Override
    public String getRewardMessage() {
        return this.exp + " exp";
    }
}
