package com.abbytech.shoppingapp.beacon;


import org.altbeacon.beacon.Region;

public class RegionStatus {
    private final Region region;
    private final boolean entered;

    public RegionStatus(Region region, boolean entered) {
        this.region = region;
        this.entered = entered;
    }

    public Region getRegion() {
        return region;
    }

    public boolean isEntered() {
        return entered;
    }

}
