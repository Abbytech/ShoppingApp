package com.abbytech.shoppingapp;


import android.app.Application;

import com.abbytech.shoppingapp.model.DaoMaster;
import com.abbytech.shoppingapp.repo.InMemoryItemRepo;
import com.abbytech.shoppingapp.repo.ItemRepo;

import org.greenrobot.greendao.database.Database;

public class ShoppingApp extends Application {
    private static ShoppingApp instance;
    private DaoMaster daoMaster;

    private ItemRepo itemRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shopping-db");
        Database db = helper.getWritableDb();
        daoMaster = new DaoMaster(db);
        itemRepo = InMemoryItemRepo.getInstance();
    }
    public ItemRepo getItemRepo() {
        return itemRepo;
    }

    public static ShoppingApp getInstance(){
        return instance;
    }
    public DaoMaster getDao(){
        return daoMaster;
    }
}
