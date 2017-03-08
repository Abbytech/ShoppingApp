package com.abbytech.util.eventform;

import rx.Observable;

public interface RequestAdapter<T, R> {
    Observable<R> getRequestObservable(T request);
}
