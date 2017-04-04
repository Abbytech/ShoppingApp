package com.abbytech.util.net;

import com.abbytech.util.eventform.SimpleResponse;

import retrofit2.adapter.rxjava.Result;
import rx.Observable;
import rx.functions.Func1;

public class Util {
    public static <T> Observable<SimpleResponse>
    mapToSimpleResponse(Observable<Result<T>> resultObservable) {
        return resultObservable.map(new Func1<Result<T>, SimpleResponse>() {
            @Override
            public SimpleResponse call(Result<T> responseBodyResult) {
                Throwable error = responseBodyResult.error();
                String message = responseBodyResult.isError() ? error.getMessage() : responseBodyResult.response().message();

                int httpCode = responseBodyResult.response().code();
                boolean success = !responseBodyResult.isError() && httpCode >= 200 && httpCode < 300;
                return new SimpleResponse
                        (success, message, error);
            }
        });
    }
}