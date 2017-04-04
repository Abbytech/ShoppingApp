package com.abbytech.login.auth;


import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class AuthenticatorProxy implements Authenticator {

    private Authenticator authenticator;

    public AuthenticatorProxy(Authenticator authenticator) {

        this.authenticator = authenticator;
    }

    public void setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
    }

    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        return authenticator.authenticate(route, response);
    }
}
