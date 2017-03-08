package com.abbytech.shoppingapp;


import java.util.ArrayList;
import java.util.List;

public class ShoppingList {
    private String name;
    private static List<ListItem> items = new ArrayList<>();

    static{
        List<Item> allItems = new InMemoryItemRepo().getAllItems();
        for (Item item : allItems) {
            items.add(new ListItem(item));
        }
    }

    public ShoppingList(String name) {
        this.name = name;
    }

    public static List<ListItem> getItems() {
        return items;
    }
}
