package com.runicrealms.plugin.unlocks;

public class ProgressUnlock extends UnlockMethod {

    private final int amountToUnlock; // some total value to unlock the achievement (10 fish)

    public ProgressUnlock(int amountToUnlock) {
        super(AchievementType.PROGRESS);
        this.amountToUnlock = amountToUnlock;
    }

    public int getAmountToUnlock() {
        return amountToUnlock;
    }
}
