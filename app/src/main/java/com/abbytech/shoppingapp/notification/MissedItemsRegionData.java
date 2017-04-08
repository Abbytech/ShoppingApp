package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.model.ListItem;

import org.altbeacon.beacon.Region;

import java.util.List;

public class MissedItemsRegionData {
    private final Region region;

    private List<ListItem> items;

    public MissedItemsRegionData(Region region, List<ListItem> items) {
        this.region = region;
        this.items = items;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public Region getRegion() {
        return region;
    }
}
