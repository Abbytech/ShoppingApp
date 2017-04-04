package com.abbytech.login.auth;

import com.abbytech.login.persistence.AccountPersister;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public abstract class BaseAuthenticator<T> implements Authenticator {
    public final AccountPersister<T> accountManager;

    BaseAuthenticator(AccountPersister<T> accountManager) {
        this.accountManager = accountManager;
    }

    protected abstract String createAuthHeaderValue(T loginData);

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (accountManager.getAccount() == null) {
            return null;
        } else {
            String authHeaderValue = createAuthHeaderValue(accountManager.getAccount());
            if (authHeaderValue.equals(response.request().header("Authorization"))) {
                return null; // If we already failed with these credentials, don't retry.
            }
            Request.Builder requestBuilder = response.request().newBuilder()
                    .header("Authorization", authHeaderValue);
            return requestBuilder.build();
        }
    }

}
