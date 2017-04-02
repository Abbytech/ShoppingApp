package com.abbytech.shoppingapp.shoppinglist;


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