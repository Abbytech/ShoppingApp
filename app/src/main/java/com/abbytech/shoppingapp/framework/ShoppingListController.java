package com.abbytech.shoppingapp.framework;


import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListRepo;

public class ShoppingListController extends ActionController<ListItem> {
    private final ShoppingListRepo shoppingListRepo;

    public ShoppingListController(Fragment fragment) {
        super(fragment);
        shoppingListRepo = ShoppingApp.getInstance().getShoppingListRepo();
    }

    @Override
    public void onItemAction(ListItem item, int action) {
        shoppingListRepo.saveShoppingItem(item);
    }
}
