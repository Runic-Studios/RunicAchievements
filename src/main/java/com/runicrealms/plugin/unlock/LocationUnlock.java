package com.runicrealms.plugin.unlock;

public class LocationUnlock extends TriggerUnlock {

    private final String regionId;

    public LocationUnlock(String regionId) {
        this.regionId = regionId;
    }

    public String getRegionId() {
        return regionId;
    }
}
