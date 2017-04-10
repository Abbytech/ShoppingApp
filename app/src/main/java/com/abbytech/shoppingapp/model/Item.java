package com.abbytech.shoppingapp.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Item {
    @Id(autoincrement = true)

    @SerializedName("item_ID")
    private Long id;


    /*@SerializedName("img_id")
    public int imageID;
    @SerializedName("img_url")
    public String imageUrl;*/





    @SerializedName("item_NAME")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("weight")
    private String weight;

    @SerializedName("origin")
    private String origin;

    @SerializedName("expire_date")
    private String exp_date;

    @SerializedName("section_ID")
    private String aisle;

    public Item(String name, String aisle) {
        this.name = name;
        this.aisle = aisle;

    }


    @Generated(hash = 1480865625)
    public Item(Long id, String name, String price, String weight, String origin,
                String exp_date, String aisle) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.weight = weight;
        this.origin = origin;
        this.exp_date = exp_date;
        this.aisle = aisle;
    }


    @Generated(hash = 1470900980)
    public Item() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;
        if (!name.equals(item.name)) return false;
        if (!price.equals(item.price)) return false;
        if (!weight.equals(item.weight)) return false;
        if (!origin.equals(item.origin)) return false;
        if (!exp_date.equals(item.exp_date)) return false;
        return aisle.equals(item.aisle);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + aisle.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + weight.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + exp_date.hashCode();
        return result;
    }

    public String getName() {
        return this.name;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getWeight() {
        return this.weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExp_date() {
        return this.exp_date.substring(0, 10);
    }

    public void setExp_date(String exp_date) {
        this.exp_date = exp_date;
    }


    /*public int getImageID() {
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
    }*/


}
