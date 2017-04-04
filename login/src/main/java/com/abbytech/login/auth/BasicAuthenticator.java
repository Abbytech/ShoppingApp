package com.abbytech.login.auth;

import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.persistence.AccountPersister;

import okhttp3.Credentials;

public class BasicAuthenticator extends BaseAuthenticator<BasicLoginData> {

    public BasicAuthenticator(AccountPersister<BasicLoginData> accountPersister) {
        super(accountPersister);
    }

    @Override
    public String createAuthHeaderValue(BasicLoginData loginData) {
        return Credentials.basic(loginData.getUsername(), loginData.getPassword());
    }

}
