package com.abbytech.shoppingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.event.LoginStateChangedEvent;
import com.abbytech.login.persistence.LoginStateManager;
import com.abbytech.shoppingapp.account.LoginActivity;

import org.greenrobot.eventbus.Subscribe;

public class SplashActivity extends AppCompatActivity {

    // Variables declaration
    ImageView imageView;
    Animation animation;
    private LoginStateManager<BasicLoginData> loginStateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loginStateManager = ShoppingApp.getInstance().getLoginStateManager();
        loginStateManager.registerLoginEventSubscriber(this);

        // Variables Initialization
        imageView = (ImageView) findViewById(R.id.splashImageView);
        animation  = AnimationUtils.loadAnimation(this, R.anim.rotate);

        // Animation execution

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                startActivity(new Intent(SplashActivity.this, MainActivity.class));

                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        imageView.setAnimation(animation);
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
