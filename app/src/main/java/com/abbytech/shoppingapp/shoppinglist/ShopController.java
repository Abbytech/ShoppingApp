package com.abbytech.shoppingapp.shoppinglist;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.framework.ActionController;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.shop.OnShopItemActionListener;

public class ShopController extends ActionController<Item> implements OnShopItemActionListener {
    private final ShoppingListManager shoppingListManager;

    public ShopController(Fragment fragment) {
        super(fragment);
        ShoppingListRepo shoppingListRepo = ShoppingApp.getInstance().getShoppingListRepo();
        shoppingListManager = new ShoppingListManager(shoppingListRepo,
                () -> shoppingListRepo.getShoppingList(1).toBlocking().first());
    }

    @Override
    public void onItemAction(Item item, int action, @Nullable Bundle extra) {

    }

    @Override
    public void onItemAction(Item item, @Action int action) {
        shoppingListManager.onItemAction(item, action);
    }
}
