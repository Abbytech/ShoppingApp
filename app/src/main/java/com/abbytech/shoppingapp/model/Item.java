package com.abbytech.shoppingapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Item {
    @Id(autoincrement = true)
    private Long id;
    private String name;
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

}
