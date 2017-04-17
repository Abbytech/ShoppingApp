package com.abbytech.shoppingapp.account;

import com.abbytech.login.LoginRequest;
import com.abbytech.login.data.BasicLoginData;
import com.abbytech.util.eventform.SimpleResponse;
import com.abbytech.util.net.APIDelegate;
import com.abbytech.util.net.SimpleRequestAdapter;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

public class RegisterAdapter extends SimpleRequestAdapter<RegisterData> {
    private APIDelegate<RegisterData, Object> registerAPI;
    private LoginAdapter loginAdapter;

    public RegisterAdapter(APIDelegate<RegisterData, Object> registerAPI, LoginAdapter loginAdapter) {
        this.registerAPI = registerAPI;
        this.loginAdapter = loginAdapter;
    }


    @Override
    public Observable<SimpleResponse> getRequestObservable(final RegisterData registerData) {
        if (registerData == null)
            throw new IllegalArgumentException("register data is null");

        Observable<SimpleResponse>
                responseObservable = super.getRequestObservable(registerData);

        Observable<SimpleResponse> loginObs =
                responseObservable
                        .filter(SimpleResponse::isSuccessful)
                        .map((Func1<SimpleResponse, LoginRequest>) submissionResponseEvent -> {
                            BasicLoginData loginData =
                                    new BasicLoginData(registerData.username, registerData.password);
                            return new LoginRequest<>(loginData);
                        }).flatMap(loginRequest -> loginAdapter.getRequestObservable(loginRequest));

        return loginObs;
    }

    @Override
    protected Observable<Result<Object>> createResponseCall(RegisterData registerData) {
        return registerAPI.perform(registerData);
    }
}
