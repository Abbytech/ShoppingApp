package com.abbytech.login.retrofit;


import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public interface APIDelegate<T, R> {
    Observable<Result<R>> login(T account);
}
