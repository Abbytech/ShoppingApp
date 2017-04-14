package com.abbytech.shoppingapp.repo;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;

import java.util.List;

import rx.Observable;

public interface IShoppingListRepo {
    Observable<ShoppingList> getShoppingList(int id);
    void saveShoppingItem(ListItem item);
    Observable<List<ShoppingList>> getShoppingLists();

    void deleteShoppingItem(ListItem item);
}
