package com.abbytech.shoppingapp;


public class Item {
    private String name;
    private String aisle;

    public Item(String name, String aisle) {
        this.name = name;
        this.aisle = aisle;
    }

    public String getName() {
        return name;
    }

    public String getAisle() {
        return aisle;
    }

}
