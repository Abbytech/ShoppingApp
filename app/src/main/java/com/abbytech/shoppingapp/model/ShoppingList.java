package com.abbytech.shoppingapp.model;


import com.abbytech.shoppingapp.repo.InMemoryItemRepo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class ShoppingList {
    private String name;
    private static List<ListItem> items = new ArrayList<>();

    static{
        Observable<List<Item>> allItemsObs = new InMemoryItemRepo().getAllItems();
        List<Item> allItems = allItemsObs.toBlocking().first();
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

    public String getName() {
        return name;
    }
}
