package com.abbytech.shoppingapp;

import android.support.test.runner.AndroidJUnit4;

import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.LocalShoppingListRepo;
import com.abbytech.shoppingapp.shop.OnItemActionListener;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListManager;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class AddItemTest {
    private ShoppingListManager manager;
    private ShoppingList list;
    private LocalShoppingListRepo repo;

    @Before
    public void setUp() throws Exception {
        repo = new LocalShoppingListRepo(ShoppingApp.getInstance().getDao());
        list = getShoppingList();
        manager = new ShoppingListManager(listener -> {}, repo,() -> list);
    }

    private ShoppingList getShoppingList() {
        return repo.getShoppingList(1).toBlocking().first();
    }

    @Test
    public void itemGetsAdded() throws Exception {
        List<ListItem> items = list.getItems();
        int sizeBefore = items.size();
        Item actual = new Item(1L, "shleem", "plumbus");
        manager.onItemAction(actual, OnItemActionListener.ACTION_ADD);
        List<ListItem> itemsAfter = list.getItems();
        Item expected = itemsAfter.get(itemsAfter.size() - 1).getItem();
        Assert.assertEquals(sizeBefore +1,itemsAfter.size());
        Assert.assertEquals(expected,actual);
    }
}
