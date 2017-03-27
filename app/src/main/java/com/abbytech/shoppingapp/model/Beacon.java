package com.abbytech.shoppingapp.model;


import com.google.gson.annotations.SerializedName;

public class Beacon {
    @SerializedName("beacon_ID")
    public String beaconID;

    public Beacon(String beaconID) {
        this.beaconID = beaconID;
    }
}
