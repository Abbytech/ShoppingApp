package com.abbytech.shoppingapp.beacon;


import com.abbytech.shoppingapp.model.ListItem;

import org.altbeacon.beacon.Region;

import java.util.List;

public class RegionEnterModel {
    private final Region region;
    private List<ListItem> items;

    public RegionEnterModel(Region region) {
        this.region = region;
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
