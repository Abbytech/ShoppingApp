package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;
import com.abbytech.shoppingapp.repo.LocalShoppingListRepo;

import java.util.List;

import rx.Observable;

public class ShoppingListRepo implements IShoppingListRepo {
    private RemoteShoppingListRepo remoteShoppingListRepo;
    private LocalShoppingListRepo localShoppingListRepo;

    public ShoppingListRepo(RemoteShoppingListRepo remoteShoppingListRepo,
                            LocalShoppingListRepo localShoppingListRepo) {
        this.remoteShoppingListRepo = remoteShoppingListRepo;
        this.localShoppingListRepo = localShoppingListRepo;
    }

    @Override
    public Observable<ShoppingList> getShoppingList(int id) {
        Observable<ShoppingList> shoppingListObservable;
        if (localShoppingListRepo.shoppingListWithIdExists(id)) {
            shoppingListObservable = localShoppingListRepo.getShoppingList(id);
        }
        else{
            shoppingListObservable = remoteShoppingListRepo.getShoppingList(id);
            shoppingListObservable.subscribe(shoppingList -> localShoppingListRepo.createShoppingList(shoppingList));
        }
        return shoppingListObservable;
    }

    @Override
    public void saveShoppingItem(ListItem item) {

    }

    @Override
    public Observable<List<ShoppingList>> getShoppingLists() {
        return null;
    }
}
