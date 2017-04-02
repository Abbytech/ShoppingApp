package com.abbytech.shoppingapp.shoppinglist;


import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.model.ListItem;

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
