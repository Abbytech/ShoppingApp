package com.abbytech.login.android;


import android.content.Context;

public class JWTAccountManager extends AccountManager<String> {
    private static final String shared_prefs_token = "token";

    public JWTAccountManager(Context context) {
        super(context);
    }

    @Override
    protected String getSharedPrefsPath() {
        return "JWT_token";
    }

    @Override
    public String getAccount() {
        return sharedPreferences.getString(shared_prefs_token, null);
    }

    @Override
    public void setAccount(String account) {
        if (account == null) {
            deleteAccount();
        } else if (account.isEmpty()) {
            throw new IllegalArgumentException("username cannot be empty");
        } else {
            sharedPreferences
                    .edit()
                    .putString(shared_prefs_token, account)
                    .putBoolean(shared_prefs_has_account, true)
                    .apply();
        }
    }
}
