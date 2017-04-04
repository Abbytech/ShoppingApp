package com.abbytech.login.android;

import android.content.Context;
import android.content.SharedPreferences;

import com.abbytech.login.persistence.AccountPersister;

public abstract class AccountManager<T> implements AccountPersister<T> {
    static final String shared_prefs_has_account = "logged_in_state";
    final SharedPreferences sharedPreferences;

    AccountManager(Context context) {
        sharedPreferences = context.getSharedPreferences(getSharedPrefsPath(),
                Context.MODE_PRIVATE);
    }

    public void deleteAccount() {
        sharedPreferences.edit().clear().apply();
    }

    protected abstract String getSharedPrefsPath();
}
