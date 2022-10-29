package com.runicrealms.plugin.rewards;

public class TitleReward extends Reward {

    private final String title;

    public TitleReward(String title) {
        super(RewardType.TITLE);
        this.title = title;
        this.setRewardMessage("Title: " + title);
    }

    public String getTitle() {
        return title;
    }
}
