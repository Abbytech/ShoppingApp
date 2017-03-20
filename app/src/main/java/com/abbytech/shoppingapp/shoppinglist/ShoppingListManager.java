package com.abbytech.shoppingapp.shoppinglist;


import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ItemDao;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.IShoppingListRepo;
import com.abbytech.shoppingapp.shop.ItemActionEmitter;
import com.abbytech.shoppingapp.shop.OnItemActionListener;

public class ShoppingListManager implements OnItemActionListener{
    private final CurrentShoppingListProvider currentShoppingListProvider;
    private IShoppingListRepo repo;
    private ItemDao itemDao;
    public ShoppingListManager(ItemActionEmitter itemActionEmitter, IShoppingListRepo repo,
                               CurrentShoppingListProvider currentShoppingListProvider, ItemDao itemDao) {
        this.currentShoppingListProvider = currentShoppingListProvider;
        this.itemDao = itemDao;
        itemActionEmitter.setOnItemActionListener(this);
        this.repo = repo;
    }

    @Override
    public void onItemAction(Item item, @Action int action) {
        ShoppingList shoppingList = currentShoppingListProvider.getCurrentShoppingList();
        switch (action) {
            case OnItemActionListener.ACTION_ADD:
                ListItem listItem = new ListItem(item,shoppingList);
                itemDao.insertOrReplace(item);
                repo.saveShoppingItem(listItem);
                shoppingList.resetItems();
                break;
        }
    }
}
