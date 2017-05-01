package com.abbytech.shoppingapp.shoppinglist;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;
import com.abbytech.shoppingapp.shop.OnShopItemActionListener;

public class ShoppingListManager implements OnShopItemActionListener {
    private final CurrentShoppingListProvider currentShoppingListProvider;
    private IShoppingListRepo repo;

    public ShoppingListManager(IShoppingListRepo repo,
                               CurrentShoppingListProvider currentShoppingListProvider) {
        this.currentShoppingListProvider = currentShoppingListProvider;
        this.repo = repo;
    }

    @Override
    public void onItemAction(Item item, int action, @Nullable Bundle extra) {

    }

    public void onItemAction(Item item, int action, @Nullable Long extras) {
        ShoppingList shoppingList = currentShoppingListProvider.getCurrentShoppingList();
        switch (action) {
            case OnShopItemActionListener.ACTION_ADD:
                ListItem existingListItem = findListItem(item.getId());
                ListItem listItem = existingListItem != null ? existingListItem : new ListItem(item, extras, shoppingList);
                if (existingListItem != null) existingListItem.addQuantity(extras);
                repo.saveShoppingItem(listItem);
                shoppingList.resetItems();
                break;
        }
    }

    private ListItem findListItem(long itemId) {
        ShoppingList shoppingList = currentShoppingListProvider.getCurrentShoppingList();
        for (ListItem listItem : shoppingList.getItems()) {
            if (listItem.getItemId() == itemId) return listItem;
        }
        return null;
    }
    @Override
    public void onItemAction(Item item, @Action int action) {
        ShoppingList shoppingList = currentShoppingListProvider.getCurrentShoppingList();
        switch (action) {
            case OnShopItemActionListener.ACTION_ADD:
                ListItem listItem = new ListItem(item,shoppingList);
                repo.saveShoppingItem(listItem);
                shoppingList.resetItems();
                break;
        }
    }
}