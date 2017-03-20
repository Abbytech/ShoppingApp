package com.abbytech.shoppingapp.model;

import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Item {
    @Id(autoincrement = true)
    @SerializedName("item_ID")
    private Long id;
    @SerializedName("item_NAME")
    private String name;
    @SerializedName("section_ID")
    private String aisle;
    public Item(String name, String aisle) {
        this.name = name;
        this.aisle = aisle;
    }

    @Generated(hash = 1028595921)
    public Item(Long id, String name, String aisle) {
        this.id = id;
        this.name = name;
        this.aisle = aisle;
    }

    @Generated(hash = 1470900980)
    public Item() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAisle() {
        return aisle;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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
        return aisle.equals(item.aisle);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + aisle.hashCode();
        return result;
    }
}
