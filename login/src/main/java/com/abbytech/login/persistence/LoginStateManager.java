package com.abbytech.login.persistence;


import com.abbytech.login.Login;
import com.abbytech.login.event.LoginStateChangedEvent;
import com.abbytech.login.event.LoginStateListener;

import org.greenrobot.eventbus.Subscribe;

public class LoginStateManager<T> implements LoginStateListener {
    private AccountPersister<T> persister;
    private Login<T> login;

    public LoginStateManager(AccountPersister<T> persister, Login<T> login) {
        this.persister = persister;
        this.login = login;
        login.registerLoginEventSubscriber(this);
    }

    public void registerLoginEventSubscriber(Object subscriber) {
        login.registerLoginEventSubscriber(subscriber);
    }

    @Override
    @Subscribe
    public void onLoginStateChanged(LoginStateChangedEvent loginStateChangedEvent) {
        if (loginStateChangedEvent.isLoggedIn()) {
            persister.setAccount((T) loginStateChangedEvent.getAccount());
        } else {
            persister.deleteAccount();
        }
    }

    public void silentLogin() {
        login.login(persister.getAccount());
    }
}
