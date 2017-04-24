package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.beacon.RegionStatus;
import com.abbytech.shoppingapp.model.Item;

import java.util.List;

public class OfferRegionData {
    private RegionStatus region;
    private List<Item> offers;

    OfferRegionData(RegionStatus region, List<Item> offers) {
        this.region = region;
        this.offers = offers;
    }

    public RegionStatus getRegionStatus() {
        return region;
    }

    public List<Item> getOffers() {
        return offers;
    }
}
