package com.abbytech.util.net;

import com.abbytech.util.eventform.RequestAdapter;
import com.abbytech.util.eventform.SimpleResponse;
import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public abstract class SimpleRequestAdapter<T>
        implements RequestAdapter<T, SimpleResponse> {

    @Override
    public Observable<SimpleResponse> getRequestObservable(T request) {
        Observable<Result<String>> responseCall = createResponseCall(request);
        onResponse(responseCall);
        return Util.mapToSimpleResponse(responseCall);
    }

    protected void onResponse(Observable<Result<String>> observable) {

    }

    protected abstract Observable<Result<String>> createResponseCall(T request);
}
