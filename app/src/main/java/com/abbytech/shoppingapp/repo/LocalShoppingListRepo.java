package com.abbytech.shoppingapp.repo;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

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
        ObservableArrayList<ListItem> items = new ObservableArrayList<>();
        items.addAll(shoppingList.getItems());
        shoppingList.setItems(items);
        items.addOnListChangedCallback(new ObservableList.OnListChangedCallback<ObservableList<ListItem>>() {
            @Override
            public void onChanged(ObservableList<ListItem> listItems) {
                shoppingList.update();
            }

            @Override
            public void onItemRangeChanged(ObservableList<ListItem> listItems, int i, int i1) {
                for (int j=i;j<i+i1;j++)
                listItems.get(j).update();
            }

            @Override
            public void onItemRangeInserted(ObservableList<ListItem> listItems, int i, int i1) {
                shoppingList.update();
            }

            @Override
            public void onItemRangeMoved(ObservableList<ListItem> listItems, int i, int i1, int i2) {
                shoppingList.update();
            }

            @Override
            public void onItemRangeRemoved(ObservableList<ListItem> listItems, int i, int i1) {
                shoppingList.update();
            }
        });
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
