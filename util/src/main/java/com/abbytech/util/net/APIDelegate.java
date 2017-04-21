package com.abbytech.util.net;


import retrofit2.adapter.rxjava.Result;
import rx.Observable;

public interface APIDelegate<T, R> {
    Observable<Result<R>> perform(T param);
}
