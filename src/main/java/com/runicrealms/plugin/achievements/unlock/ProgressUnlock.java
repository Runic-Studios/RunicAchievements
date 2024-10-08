package com.runicrealms.plugin.achievements.unlock;

import com.runicrealms.plugin.achievements.api.UnlockMethod;

public class ProgressUnlock implements UnlockMethod {

    private final int amountToUnlock; // some total value to unlock the achievement (10 fish)

    public ProgressUnlock(int amountToUnlock) {
        super();
        this.amountToUnlock = amountToUnlock;
    }

    public int getAmountToUnlock() {
        return amountToUnlock;
    }
}
