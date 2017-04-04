package com.abbytech.login.android;

import android.content.Context;
import android.text.TextUtils;

import com.abbytech.login.data.BasicLoginData;

public class BasicAccountManager extends AccountManager<BasicLoginData> {
    private static final String shared_prefs_username = "username";
    private static final String shared_prefs_password = "password";

    public BasicAccountManager(Context context) {
        super(context);
    }

    @Override
    protected String getSharedPrefsPath() {
        return "basic_account";
    }

    public BasicLoginData getAccount() {
        String username = sharedPreferences.getString(shared_prefs_username, "");
        if (TextUtils.isEmpty(username)) return null;
        String password = sharedPreferences.getString(shared_prefs_password, "");
        return new BasicLoginData(username, password);
    }

    public void setAccount(BasicLoginData basicLoginData) {
        if (basicLoginData == null) {
            deleteAccount();
        } else if (basicLoginData.getUsername().isEmpty()) {
            throw new IllegalArgumentException("username cannot be empty");
        } else if (basicLoginData.getPassword().isEmpty())
            throw new IllegalArgumentException("password cannot be empty");
        else {
            sharedPreferences
                    .edit()
                    .putString(shared_prefs_username, basicLoginData.getUsername())
                    .putString(shared_prefs_password, basicLoginData.getPassword())
                    .putBoolean(shared_prefs_has_account, true)
                    .apply();
        }
    }
}
