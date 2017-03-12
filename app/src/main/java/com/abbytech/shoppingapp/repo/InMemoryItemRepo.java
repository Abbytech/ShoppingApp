package com.abbytech.shoppingapp.repo;


import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ItemDao;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class InMemoryItemRepo implements ItemRepo {
    private static ItemRepo instance;

    private final ItemDao itemDao;

    public InMemoryItemRepo() {
        itemDao = ShoppingApp.getInstance().getDao().newSession().getItemDao();
        if (itemDao.load(1L)==null) {
            List<Item> items = new ArrayList<>();
            items.add(new Item("Cheese", "Dairy"));
            items.add(new Item("Milk", "Dairy"));
            items.add(new Item("Yogurt", "Dairy"));
            for (Item item : items) {
                itemDao.save(item);
            }
        }
    }
    public static ItemRepo getInstance(){
        if (instance==null)
            instance = new InMemoryItemRepo();
        return instance;
    }
    @Override
    public Observable<List<Item>> getAllItems() {
        List<Item> items = itemDao.loadAll();
        return Observable.just(items);
    }
}
