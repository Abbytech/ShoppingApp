package com.abbytech.shoppingapp;


import com.abbytech.shoppingapp.model.ShoppingList;
import com.abbytech.shoppingapp.shoppinglist.RemoteShoppingListRepo;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListAPI;
import com.abbytech.shoppingapp.util.PropertiesLoader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Retrofit;

public class RemoteShoppingListRepoTest {

    private RemoteShoppingListRepo repo;

    @Before
    public void setUp() throws Exception {
        Retrofit retrofit = RetrofitFactory.createRetrofit(PropertiesLoader.loadProperties("net"));
        repo = new RemoteShoppingListRepo(retrofit.create(ShoppingListAPI.class));
    }

    @Test
    public void getsShoppingList() throws Exception {
        ShoppingList list = repo.getShoppingList(0).toBlocking().first();
        Assert.assertNotNull(list);
        Assert.assertFalse(list.getItems().isEmpty());
    }
}
