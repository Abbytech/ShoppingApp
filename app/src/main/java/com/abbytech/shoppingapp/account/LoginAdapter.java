package com.abbytech.shoppingapp.account;

import com.abbytech.login.Login;
import com.abbytech.login.LoginRequest;
import com.abbytech.login.auth.AuthenticatorProxy;
import com.abbytech.login.auth.BasicAuthenticator;
import com.abbytech.login.auth.BearerAuthenticator;
import com.abbytech.login.data.BasicLoginData;
import com.abbytech.login.event.LoginStateChangedEvent;
import com.abbytech.util.net.APIDelegate;
import com.abbytech.util.eventform.SimpleObserver;
import com.abbytech.util.eventform.SimpleResponse;
import com.abbytech.util.net.SimpleRequestAdapter;

import org.greenrobot.eventbus.EventBus;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public class LoginAdapter extends SimpleRequestAdapter<LoginRequest> implements Login<BasicLoginData> {
    private APIDelegate<BasicLoginData, Object> api;
    private BasicAuthenticator basicAuthenticator;
    private BearerAuthenticator bearerAuthenticator;
    private AuthenticatorProxy proxy;
    private EventBus eventBus = new EventBus();

    public LoginAdapter(APIDelegate<BasicLoginData, Object> api, BasicAuthenticator basicAuthenticator,
                        BearerAuthenticator bearerAuthenticator, AuthenticatorProxy proxy) {
        this.api = api;
        this.basicAuthenticator = basicAuthenticator;
        this.bearerAuthenticator = bearerAuthenticator;
        this.proxy = proxy;
    }

    @Override
    public Observable<SimpleResponse> getRequestObservable(final LoginRequest request) {
        basicAuthenticator.accountManager.setAccount((BasicLoginData) request.getExtra());
        proxy.setAuthenticator(basicAuthenticator);
        return super.getRequestObservable(request);
    }

    @Override
    protected <S> void onResponse(Observable<SimpleResponse> observable, LoginRequest request) {
        observable.subscribe(new SimpleObserver<SimpleResponse>() {
            @Override
            public void onNext(SimpleResponse sResult) {
                LoginStateChangedEvent event = new LoginStateChangedEvent(sResult.isSuccessful());
                event.setAccount(request.getExtra());
                onLoginStateChanged(event);
            }
        });
    }

    @Override
    public void logout() {
        onLoginStateChanged(new LoginStateChangedEvent(false));
    }

    @Override
    public void onLoginStateChanged(LoginStateChangedEvent event) {
        eventBus.post(event);
    }

    @Override
    public void login(BasicLoginData account) {
        basicAuthenticator.accountManager.setAccount(account);
        proxy.setAuthenticator(basicAuthenticator);
        getRequestObservable(new LoginRequest<>(account)).subscribe();
    }

    @Override
    protected Observable<Result<Object>> createResponseCall(LoginRequest request) {
        BasicLoginData account = (BasicLoginData) request.getExtra();
        return api.perform(account);
    }

    @Override
    public void registerLoginEventSubscriber(Object subscriber) {
        eventBus.register(subscriber);
    }
}
