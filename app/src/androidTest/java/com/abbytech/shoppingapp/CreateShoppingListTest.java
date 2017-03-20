package com.abbytech.shoppingapp;


import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.model.ListItem;
import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.repo.LocalShoppingListRepo;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CreateShoppingListTest {
    private ShoppingList list;
    private LocalShoppingListRepo repo;

    private int shoppingListId = 1;

    {
        list = new ShoppingList((long) shoppingListId,"Stuff");
        List<ListItem> listItems = new ArrayList<>(1);
        Item item = new Item(1L,"Broccoli","Produce");
        listItems.add(new ListItem(item,list));
        list.setItems(listItems);
    }
    @Before
    public void setUp() throws Exception {
        repo = new LocalShoppingListRepo(ShoppingApp.getInstance().getDao());
        Assert.assertFalse(repo.shoppingListWithIdExists(shoppingListId));
    }

    @Test
    public void listCorrectlyCreated() throws Exception {
        repo.createShoppingList(list);
        ShoppingList shoppingList = repo.getShoppingList(shoppingListId).toBlocking().first();
        Assert.assertNotNull(shoppingList);
        List<ListItem> listItems = shoppingList.getItems();
        Assert.assertNotNull(listItems);
        Assert.assertFalse(listItems.isEmpty());
        Assert.assertNotNull(listItems.get(0).getItem());
    }
}
