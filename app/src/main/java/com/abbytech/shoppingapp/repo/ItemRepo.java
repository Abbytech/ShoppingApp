package com.abbytech.shoppingapp.repo;


import com.abbytech.shoppingapp.model.Item;

import java.util.List;

import rx.Observable;

public interface ItemRepo {
    Observable<List<Item>> getAllItems();

    Observable<List<Item>> getAllItems(String aisle);
}
