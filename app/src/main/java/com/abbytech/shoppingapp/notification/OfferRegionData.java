package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.model.Item;

import org.altbeacon.beacon.Region;

import java.util.List;

public class OfferRegionData {
    private Region region;
    private List<Item> offers;

    public OfferRegionData(Region region, List<Item> offers) {
        this.region = region;
        this.offers = offers;
    }

    public Region getRegion() {
        return region;
    }

    public List<Item> getOffers() {
        return offers;
    }
}
