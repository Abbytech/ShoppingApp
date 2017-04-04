package com.abbytech.login.auth;

import com.abbytech.login.persistence.AccountPersister;

public class BearerAuthenticator extends BaseAuthenticator<String> {

    public BearerAuthenticator(AccountPersister<String> accountManager) {
        super(accountManager);
    }

    @Override
    public String createAuthHeaderValue(String token) {
        return "Bearer " + token;
    }

}
