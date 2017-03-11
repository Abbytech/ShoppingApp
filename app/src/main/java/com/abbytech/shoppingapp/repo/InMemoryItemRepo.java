package com.abbytech.shoppingapp.repo;


import com.abbytech.shoppingapp.model.Item;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class InMemoryItemRepo implements ItemRepo {
    List<Item> items = new ArrayList<>();

    public InMemoryItemRepo() {
        items.add(new Item("Cheese","Dairy"));
        items.add(new Item("Milk","Dairy"));
        items.add(new Item("Yogurt","Dairy"));
    }

    @Override
    public Observable<List<Item>> getAllItems() {
        return Observable.just(items);
    }
}
