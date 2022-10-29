package com.runicrealms.plugin.api;

import com.runicrealms.plugin.reward.RewardType;

public abstract class Reward {

    private final RewardType rewardType;
    private String rewardMessage;

    public Reward(RewardType rewardType) {
        this.rewardType = rewardType;
        this.rewardMessage = "";
    }

    public Reward(RewardType rewardType, String rewardMessage) {
        this.rewardType = rewardType;
        this.rewardMessage = rewardMessage;
    }

    public RewardType getRewardType() {
        return this.rewardType;
    }

    public String getRewardMessage() {
        return rewardMessage;
    }

    public void setRewardMessage(String rewardMessage) {
        this.rewardMessage = rewardMessage;
    }
}
