package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;

import java.util.List;

import rx.Observable;

public class RemoteShoppingListRepo implements IShoppingListRepo {
    ShoppingListAPI api;

    public RemoteShoppingListRepo(ShoppingListAPI api) {
        this.api = api;
    }

    @Override
    public Observable<ShoppingList> getShoppingList(int id) {
        return api.getShoppingList(id);
    }

    @Override
    public void saveShoppingItem(ListItem item) {
        api.saveShoppingItem(item).subscribe();
    }

    @Override
    public Observable<List<ShoppingList>> getShoppingLists() {
        return api.getShoppingLists();
    }
}
