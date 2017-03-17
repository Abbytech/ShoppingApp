package com.abbytech.shoppingapp;


import com.abbytech.shoppingapp.model.Item;
import com.abbytech.shoppingapp.shop.ShopAPI;
import com.abbytech.shoppingapp.util.PropertiesLoader;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Properties;

import retrofit2.Retrofit;

public class APITest {

    private ShopAPI shopAPI;

    @Before
    public void setUp() throws Exception {
        Properties properties = PropertiesLoader.loadProperties("net");
        Retrofit retrofit = RetrofitFactory.createRetrofit(properties);
        shopAPI = retrofit.create(ShopAPI.class);
    }

    @Test
    public void testGetAllItems() throws Exception {
        List<Item> first = shopAPI.getItems().toBlocking().first();
        Assert.assertNotNull(first);
    }

    @Test
    public void testGetItem() throws Exception {
        Item first = shopAPI.getItem(1).toBlocking().first();
        Assert.assertNotNull(first);
    }
}