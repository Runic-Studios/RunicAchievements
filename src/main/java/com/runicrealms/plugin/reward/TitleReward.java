package com.runicrealms.plugin.reward;

public class TitleReward extends Reward {

    private final String title;
    private final boolean isSuffix;

    public TitleReward(String title, boolean isSuffix) {
        super(RewardType.TITLE);
        this.title = title;
        this.isSuffix = isSuffix;
        this.setRewardMessage("Title: " + title);
    }

    public String getTitle() {
        return title;
    }

    public boolean isSuffix() {
        return isSuffix;
    }
}
