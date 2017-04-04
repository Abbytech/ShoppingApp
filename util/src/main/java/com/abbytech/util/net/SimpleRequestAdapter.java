package com.abbytech.util.net;

import com.abbytech.util.eventform.RequestAdapter;
import com.abbytech.util.eventform.SimpleResponse;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public abstract class SimpleRequestAdapter<T>
        implements RequestAdapter<T, SimpleResponse> {

    @Override
    public Observable<SimpleResponse> getRequestObservable(T request) {
        Observable<Result<Object>> responseCall = createResponseCall(request);
        Observable<SimpleResponse> simpleResponseObservable = Util.mapToSimpleResponse(responseCall);
        onResponse(simpleResponseObservable, request);
        return simpleResponseObservable;
    }

    protected <S> void onResponse(Observable<SimpleResponse> observable, T request) {

    }

    protected abstract Observable<Result<Object>> createResponseCall(T request);
}
