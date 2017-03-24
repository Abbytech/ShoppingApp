package com.abbytech.shoppingapp.beacon;


import android.content.Context;

import com.google.gson.Gson;

import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.Region;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class RegionFactory {
    public static Region createRegion(String regionName,String advertisementId){
        Identifier advertisementIdentifier = Identifier.parse(advertisementId);
        return new Region(regionName,Constant.venueID,advertisementIdentifier,null);
    }

    public static Region createRegion(RegionData data) {
        return createRegion(data.name, data.id);
    }

    public static Region[] getRegionsFromResources(Context context) throws IOException {
        InputStream inputStream = context.getClassLoader().getResourceAsStream("beacons.json");
        String string = IOUtils.toString(inputStream);
        Gson gson = new Gson();
        RegionData[] regionDatas = gson.fromJson(string, RegionData[].class);
        Region[] regions = new Region[regionDatas.length];
        for (int i = 0; i < regionDatas.length; i++) {
            regions[i] = createRegion(regionDatas[i]);
        }
        return regions;
    }
}
