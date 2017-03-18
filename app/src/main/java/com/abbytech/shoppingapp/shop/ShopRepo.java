package com.abbytech.shoppingapp.shop;


import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.repo.ItemRepo;

import java.util.List;

import rx.Observable;

public class ShopRepo implements ItemRepo {
    ShopAPI api;

    public ShopRepo(ShopAPI api) {
        this.api = api;
    }

    @Override
    public Observable<List<Item>> getAllItems() {
        return api.getItems();
    }
}