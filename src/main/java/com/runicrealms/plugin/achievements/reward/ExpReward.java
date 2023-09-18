package com.runicrealms.plugin.achievements.reward;

import com.runicrealms.plugin.achievements.api.Reward;

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
