package com.abbytech.shoppingapp.repo;


import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;

import com.abbytech.shoppingapp.ShoppingApp;
import com.abbytech.shoppingapp.model.DaoSession;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ListItemDao;
import com.abbytech.shoppingapp.model.ShoppingList;

import java.util.List;

import rx.Observable;

public class LocalShoppingListRepo implements IShoppingListRepo {
    private static LocalShoppingListRepo ourInstance;
    private final DaoSession daoSession;
    private long shoppingListId = 1;

    public static LocalShoppingListRepo getInstance() {
        if (ourInstance==null)
            ourInstance = new LocalShoppingListRepo(ShoppingApp.getInstance().getDao());
        return ourInstance;
    }

    private LocalShoppingListRepo(DaoSession session) {
        daoSession = session;
        if (daoSession.getShoppingListDao().load(shoppingListId)==null) {
            ShoppingList shoppingList = new ShoppingList("Groceries");
            shoppingListId = daoSession.insert(shoppingList);
        }
    }
    @Override
    public void saveShoppingItem(ListItem listItem){
        ListItemDao listItemDao = daoSession.getListItemDao();
        listItemDao.save(listItem);
    }
    @Override
    public Observable<ShoppingList> getShoppingList(int id) {
        ShoppingList shoppingList = daoSession.getShoppingListDao().load(shoppingListId);
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

    @Override
    public Observable<List<ShoppingList>> getShoppingLists() {
        return null;
    }
}
