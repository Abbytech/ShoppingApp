package com.abbytech.shoppingapp;


import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.abbytech.login.android.BasicAccountManager;
import com.abbytech.login.auth.AuthenticatorProxy;
import com.abbytech.login.auth.BasicAuthenticator;
import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.persistence.AccountPersister;
import com.abbytech.login.persistence.LoginStateManager;
import com.abbytech.shoppingapp.account.AccountAPI;
import com.abbytech.shoppingapp.account.LoginAdapter;
import com.abbytech.shoppingapp.account.RegisterAdapter;
import com.abbytech.shoppingapp.model.DaoMaster;
import com.abbytech.shoppingapp.model.DaoSession;
import com.abbytech.shoppingapp.notification.LocationAPI;
import com.abbytech.shoppingapp.notification.ZoneAlertService;
import com.abbytech.shoppingapp.repo.LocalShoppingListRepo;
import com.abbytech.shoppingapp.shop.ShopAPI;
import com.abbytech.shoppingapp.shop.ShopRepo;
import com.abbytech.shoppingapp.shoppinglist.RemoteShoppingListRepo;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListAPI;
import com.abbytech.shoppingapp.shoppinglist.ShoppingListRepo;
import com.abbytech.shoppingapp.util.PropertiesLoader;
import com.abbytech.shoppingapp.util.ServiceUtils;

import org.greenrobot.greendao.AbstractDao;
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
    private Retrofit retrofit;

    private ShopRepo shopRepo;

    private ShoppingListRepo shoppingListRepo;

    private LocationAPI locationAPI;
    private BasicAccountManager accountPersister;
    private LoginStateManager<BasicLoginData> loginStateManager;
    private LoginAdapter loginAdapter;
    private BasicAuthenticator basicAuthenticator;
    private AuthenticatorProxy authenticatorProxy;
    private RegisterAdapter registerAdapter;
    private AccountAPI accountAPI;

    public static ShoppingApp getInstance() {
        return instance;
    }

    public AccountPersister getAccountManager() {
        return accountPersister;
    }

    public AccountAPI getAccountAPI() {
        if (accountAPI == null) accountAPI = getRetrofit().create(AccountAPI.class);
        return accountAPI;
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
        retrofit = createRetrofit();
        shopRepo = new ShopRepo(retrofit.create(ShopAPI.class));
        shoppingListRepo = new ShoppingListRepo(new RemoteShoppingListRepo(retrofit.create(ShoppingListAPI.class)),
                new LocalShoppingListRepo(daoMaster));
        locationAPI = retrofit.create(LocationAPI.class);
        loginAdapter = createLoginAdapter();
        loginStateManager = new LoginStateManager<>(accountPersister, loginAdapter);
        toggleZoneAlertService();
    }

    private void toggleZoneAlertService() {
        SharedPreferences sp =
                getSharedPreferences(getString(R.string.shared_prefs_settings), MODE_PRIVATE);
        String key = getString(R.string.PREFS_KEY_ZONE_ALERTS);
        boolean zoneAlerts = sp.getBoolean(key, false);
        ServiceUtils.startStopService(this,
                new Intent(this, ZoneAlertService.class), zoneAlerts);
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

    private LoginAdapter createLoginAdapter() {
        return new LoginAdapter(account -> getAccountAPI().login().map(responseBodyResult ->
                Result.response((Response) responseBodyResult.response())),
                getBasicAuthenticator(), null, getAuthenticatorProxy());

    }

    public RegisterAdapter getRegisterAdapter() {
        if (registerAdapter == null) {
            registerAdapter = new RegisterAdapter(param -> getAccountAPI()
                    .register(param).map(responseBodyResult ->
                            Result.response((Response) responseBodyResult.response())), getLoginAdapter());
        }
        return registerAdapter;
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

    public void logout() {
        getAccountManager().deleteAccount();
        for (AbstractDao<?, ?> dao : getDao().getAllDaos()) dao.deleteAll();
    }

    public String getUsername() {
        return accountPersister.getAccount().getUsername();
    }
}
