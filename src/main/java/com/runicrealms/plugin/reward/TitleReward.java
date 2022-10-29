package com.runicrealms.plugin.reward;

import com.runicrealms.plugin.api.Reward;

public class TitleReward implements Reward {

    private final String title;
    private final boolean isSuffix;

    public TitleReward(String title, boolean isSuffix) {
        this.title = title;
        this.isSuffix = isSuffix;
    }

    public String getTitle() {
        return title;
    }

    public boolean isSuffix() {
        return isSuffix;
    }

    @Override
    public String getRewardMessage() {
        return "Title: " + this.title;
    }
}
