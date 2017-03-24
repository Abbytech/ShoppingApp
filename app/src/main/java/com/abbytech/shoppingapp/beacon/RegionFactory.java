package com.abbytech.shoppingapp.beacon;


import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;

public class RegionFactory {
    public static Region createRegion(String regionName,String advertisementId){
        Identifier advertisementIdentifier = Identifier.parse(advertisementId);
        return new Region(regionName,Constant.venueID,advertisementIdentifier,null);
    }
}
