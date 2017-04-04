package com.abbytech.shoppingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.event.LoginStateChangedEvent;
import com.abbytech.login.persistence.LoginStateManager;
import com.abbytech.shoppingapp.account.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends AppCompatActivity {

    private LoginStateManager<BasicLoginData> loginStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginStateManager = ShoppingApp.getInstance().getLoginStateManager();
        loginStateManager.registerLoginEventSubscriber(this);
    }

    @Subscribe
    public void onLoginEvent(LoginStateChangedEvent event) {
        if (event.isLoggedIn()) navigate(MainActivity.class);
        else navigate(LoginActivity.class);
    }

    private void navigate(Class<? extends Activity> activity) {
        startActivity(new Intent(this, activity));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginStateManager.silentLogin();
    }
}
