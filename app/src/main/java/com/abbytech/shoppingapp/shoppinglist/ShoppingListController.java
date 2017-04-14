package com.abbytech.shoppingapp.shoppinglist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.model.ListItem;

public class ShoppingListController extends ActionController<ListItem> implements OnShoppingListItemActionListener {
    private final ShoppingListRepo shoppingListRepo;

    public ShoppingListController(Fragment fragment) {
        super(fragment);
        shoppingListRepo = ShoppingApp.getInstance().getShoppingListRepo();
    }

    @Override
    public void onItemAction(ListItem item, @Action int action) {
        switch (action) {
            case OnShoppingListItemActionListener.ACTION_CHECK:
                shoppingListRepo.saveShoppingItem(item);
                break;
            case OnShoppingListItemActionListener.ACTION_DELETE:
                shoppingListRepo.deleteShoppingItem(item);
                break;
            case OnShoppingListItemActionListener.ACTION_MODIFY:
                break;
        }

    }

    @Override
    public void onItemAction(ListItem item, @Action int action, @Nullable Bundle extra) {

    }
}
