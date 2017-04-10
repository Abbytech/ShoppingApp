package com.abbytech.shoppingapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by USER on 4/8/2017.
 */

public class Image {

    @SerializedName("img_id")
    public int imageID;
    @SerializedName("img_url")
    public String imageUrl;
    @SerializedName("item_ID")
    public int itemid;


    public Image(int imageID, String imageUrl, int itemid) {
        this.imageID = imageID;
        this.imageUrl = imageUrl;
        this.itemid = itemid;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (getImageID() != image.getImageID()) return false;
        if (getItemid() != image.getItemid()) return false;
        return getImageUrl().equals(image.getImageUrl());
    }

    @Override
    public int hashCode() {
        int result = getImageID();
        result = 31 * result + getImageUrl().hashCode();
        result = 31 * result + getItemid();
        return result;
    }
}

