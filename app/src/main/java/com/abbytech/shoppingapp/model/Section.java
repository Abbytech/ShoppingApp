package com.abbytech.shoppingapp.model;


import com.google.gson.annotations.SerializedName;

import org.altbeacon.beacon.Region;

public class Section {
    @SerializedName("beacon_ID")
    private String sectionID;

    public Section(String sectionID) {
        this.sectionID = sectionID;
    }

    public static Section fromRegion(Region region) {
        String sectionID = region.getId2().toHexString().substring(2);
        return new Section(sectionID);
    }

    public String getSectionID() {
        return sectionID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Section section = (Section) o;

        return sectionID.equals(section.sectionID);
    }

    @Override
    public int hashCode() {
        return sectionID.hashCode();
    }
}
