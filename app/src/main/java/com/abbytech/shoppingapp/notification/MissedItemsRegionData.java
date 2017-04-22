package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.beacon.RegionStatus;
import com.abbytech.shoppingapp.model.ListItem;

import java.util.List;

public class MissedItemsRegionData {
    private final RegionStatus region;

    private List<ListItem> items;

    MissedItemsRegionData(RegionStatus region, List<ListItem> items) {
        this.region = region;
        this.items = items;
    }

    public List<ListItem> getItems() {
        return items;
    }

    public RegionStatus getRegionStatus() {
        return region;
    }
}
