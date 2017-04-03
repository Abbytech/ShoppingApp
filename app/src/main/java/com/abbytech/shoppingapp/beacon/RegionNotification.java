package com.abbytech.shoppingapp.beacon;


import com.abbytech.shoppingapp.model.ListItem;

import org.altbeacon.beacon.Region;

import java.util.List;

public class RegionNotification {
    private final Region region;

    private List<ListItem> items;

    public RegionNotification(Region region, List<ListItem> items) {
        this.region = region;
        this.items = items;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public void setItems(List<ListItem> items) {
        this.items = items;
    }

    public Region getRegion() {
        return region;
    }

}
