package com.abbytech.shoppingapp;


import android.app.Application;
import android.util.Log;

import com.abbytech.shoppingapp.model.DaoMaster;
import com.abbytech.shoppingapp.repo.InMemoryItemRepo;
import com.abbytech.shoppingapp.repo.ItemRepo;
import com.abbytech.shoppingapp.shop.ShopAPI;
import com.abbytech.shoppingapp.shop.ShopRepo;
import com.abbytech.shoppingapp.util.PropertiesLoader;

import org.greenrobot.greendao.database.Database;

import java.io.IOException;
import java.util.Properties;

import retrofit2.Retrofit;

public class ShoppingApp extends Application {
    private static final String TAG = "ShoppingApp";
    private static ShoppingApp instance;
    private DaoMaster daoMaster;
    private ItemRepo itemRepo;
    private Retrofit retrofit;

    private ShopRepo shopRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shopping-db");
        Database db = helper.getWritableDb();
        daoMaster = new DaoMaster(db);
        itemRepo = InMemoryItemRepo.getInstance();
        retrofit = createRetrofit();
        shopRepo = new ShopRepo(retrofit.create(ShopAPI.class));
    }

    private Retrofit createRetrofit() {
        try {
            Properties netProperties = PropertiesLoader.loadProperties("net");
            return RetrofitFactory.createRetrofit(netProperties);
        } catch (IOException e) {
            Log.d(TAG, "createRetrofit: "+"failed to load properties",e);
        }
        return null;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ItemRepo getItemRepo() {
        return itemRepo;
    }

    public ShopRepo getShopRepo() {
        return shopRepo;
    }

    public static ShoppingApp getInstance(){
        return instance;
    }
    public DaoMaster getDao(){
        return daoMaster;
    }
}
