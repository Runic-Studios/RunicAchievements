package com.runicrealms.plugin.unlocks;

public class LocationUnlock extends TriggerUnlock {

    private final String regionId;

    public LocationUnlock(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionId() {
        return regionId;
    }
}
