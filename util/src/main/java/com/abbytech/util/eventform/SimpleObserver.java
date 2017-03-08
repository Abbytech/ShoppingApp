package com.abbytech.util.eventform;

import rx.Observer;

public abstract class SimpleObserver<T> implements Observer<T> {
    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }
}
