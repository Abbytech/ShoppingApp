package com.abbytech.shoppingapp;


import java.util.ArrayList;
import java.util.List;

public class InMemoryItemRepo implements ItemRepo {
    List<Item> items = new ArrayList<>();

    public InMemoryItemRepo() {
        items.add(new Item("Cheese","Dairy"));
        items.add(new Item("Milk","Dairy"));
        items.add(new Item("Yogurt","Dairy"));
    }

    @Override
    public List<Item> getAllItems() {
        return items;
    }
}
