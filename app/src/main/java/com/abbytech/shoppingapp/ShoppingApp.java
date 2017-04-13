package com.abbytech.shoppingapp;


import android.app.Application;
import android.util.Log;

import com.abbytech.login.android.BasicAccountManager;
import com.abbytech.login.auth.AuthenticatorProxy;
import com.abbytech.login.auth.BasicAuthenticator;
import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.persistence.LoginStateManager;
import com.abbytech.shoppingapp.account.LoginActivity;
import com.abbytech.shoppingapp.account.LoginAdapter;
import com.abbytech.shoppingapp.model.DaoMaster;
import com.abbytech.shoppingapp.model.DaoSession;
import com.abbytech.shoppingapp.notification.LocationAPI;
import com.abbytech.shoppingapp.repo.ItemRepo;
import com.abbytech.shoppingapp.repo.LocalShoppingListRepo;
import com.abbytech.shoppingapp.shop.ShopAPI;
import com.abbytech.shoppingapp.shop.ShopRepo;
import com.abbytech.shoppingapp.shoppinglist.RemoteShoppingListRepo;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListAPI;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListRepo;
import com.abbytech.shoppingapp.util.PropertiesLoader;

import org.greenrobot.greendao.database.Database;

import java.io.IOException;
import java.util.Properties;

import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;

public class ShoppingApp extends Application {
    private static final String TAG = "ShoppingApp";
    private static ShoppingApp instance;
    private DaoSession daoMaster;
    private ItemRepo itemRepo;
    private Retrofit retrofit;

    private ShopRepo shopRepo;

    private ShoppingListRepo shoppingListRepo;

    private LocationAPI locationAPI;
    private BasicAccountManager accountPersister;
    private LoginStateManager<BasicLoginData> loginStateManager;
    private LoginAdapter loginAdapter;
    private BasicAuthenticator basicAuthenticator;
    private AuthenticatorProxy authenticatorProxy;

    public static ShoppingApp getInstance() {
        return instance;
    }

    public LoginStateManager<BasicLoginData> getLoginStateManager() {
        return loginStateManager;
    }

    public LoginAdapter getLoginAdapter() {
        return loginAdapter;
    }

    public BasicAuthenticator getBasicAuthenticator() {
        return basicAuthenticator;
    }

    public AuthenticatorProxy getAuthenticatorProxy() {
        return authenticatorProxy;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "shopping-db");
        Database db = helper.getWritableDb();
        daoMaster = new DaoMaster(db).newSession();
        itemRepo = null;
        retrofit = createRetrofit();
        shopRepo = new ShopRepo(retrofit.create(ShopAPI.class));
        shoppingListRepo = new ShoppingListRepo(new RemoteShoppingListRepo(retrofit.create(ShoppingListAPI.class)),
                new LocalShoppingListRepo(daoMaster));
        locationAPI = retrofit.create(LocationAPI.class);
        loginAdapter = createAdapter();
        loginStateManager = new LoginStateManager<>(accountPersister, loginAdapter);
    }

    private Retrofit createRetrofit() {
        try {
            Properties netProperties = PropertiesLoader.loadProperties("net");
            return RetrofitFactory.createRetrofit(netProperties, createClientBuilder());
        } catch (IOException e) {
            Log.d(TAG, "createRetrofit: " + "failed to load properties", e);
        }
        return null;
    }

    private LoginAdapter createAdapter() {
        LoginActivity.LoginAPI loginAPI = ShoppingApp.getInstance().getRetrofit().create(LoginActivity.LoginAPI.class);
        return new LoginAdapter(account -> loginAPI.login().map(responseBodyResult -> {
            Result<Object> obs = Result.response((Response) responseBodyResult.response());
            return obs;
        }), getBasicAuthenticator(), null, getAuthenticatorProxy());

    }

    private OkHttpClient.Builder createClientBuilder() {
        accountPersister = new BasicAccountManager(this);
        basicAuthenticator = new BasicAuthenticator(accountPersister);
        authenticatorProxy = new AuthenticatorProxy(basicAuthenticator);
        return new OkHttpClient.Builder()
                .authenticator(authenticatorProxy);
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

    public ShoppingListRepo getShoppingListRepo() {
        return shoppingListRepo;
    }

    public LocationAPI getLocationAPI() {
        return locationAPI;
    }

    public DaoSession getDao() {
        return daoMaster;
    }
}
