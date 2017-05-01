package com.abbytech.shoppingapp.repo;


import com.abbytech.shoppingapp.model.DaoSession;
import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ListItemDao;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.model.ShoppingListDao;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;

public class LocalShoppingListRepo implements IShoppingListRepo {
    private final DaoSession daoSession;

    public LocalShoppingListRepo(DaoSession session) {
        daoSession = session;
    }
    @Override
    public void saveShoppingItem(ListItem listItem){
        daoSession.getItemDao().insertOrReplace(listItem.getItem());
        daoSession.getListItemDao().insertOrReplace(listItem);
    }
    @Override
    public Observable<ShoppingList> getShoppingList(int id) {
        ShoppingList shoppingList = loadShoppingList(id);
        return Observable.just(shoppingList);
    }

    private ShoppingList loadShoppingList(long id) {
        ShoppingList shoppingList = daoSession.getShoppingListDao().load(id);
        shoppingList.resetItems();
        return shoppingList;
    }
    public void createShoppingList(ShoppingList list){
        ShoppingListDao shoppingListDao = daoSession.getShoppingListDao();
        List<ListItem> listItems = list.getItems();
        if (listItems!=null&&!listItems.isEmpty()) {
            List<Item> items = new ArrayList<>();
            for (ListItem item:listItems) {
                item.setShoppingListId(list.getId());
                items.add(item.getItem());
            }
            daoSession.getListItemDao().insertOrReplaceInTx(listItems);
            daoSession.getItemDao().insertOrReplaceInTx(items);
        }
        shoppingListDao.insertOrReplace(list);
        list.resetItems();
    }
    @Override
    public Observable<List<ShoppingList>> getShoppingLists() {
        return null;
    }

    @Override
    public void deleteShoppingItem(ListItem item) {
        ListItemDao listItemDao = daoSession.getListItemDao();
        listItemDao.delete(item);
    }

    public boolean shoppingListWithIdExists(long id) {
        return (daoSession.getShoppingListDao().load(id) != null);
    }
}
