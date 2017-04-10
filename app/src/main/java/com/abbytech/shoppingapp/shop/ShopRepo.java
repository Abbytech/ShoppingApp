package com.abbytech.shoppingapp.shop;


import com.abbytech.shoppingapp.model.Image;
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

    public Observable<List<Item>> getAllItems(String section) {
        return api.getItems(section);
    }

    public Observable<Image> getImage(int id) {
        return api.getImage(id);
    }

    public Observable<Item> getItem(String id) {
        return api.getItem(id);
    }
}
