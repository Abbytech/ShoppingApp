package com.abbytech.util.eventform;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.abbytech.util.form.Submittable;

import rx.Observable;

public abstract class RequestFragment<T> extends Fragment implements Submittable<T> {
    private SimpleRequestHandler requestHandler;
    private RequestAdapter<T, SimpleResponse> adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestHandler = setRequestHandler();
        setRetainInstance(true);
    }

    protected abstract SimpleRequestHandler setRequestHandler();

    @Override
    public void onSubmit(T formData) {
        if (adapter == null)
            throw new IllegalStateException("request adapter is null");
        if (requestHandler == null)
            throw new IllegalStateException("request handler is null");

        Observable<SimpleResponse> requestObservable = adapter.getRequestObservable(formData);
        requestHandler.doRequest(requestObservable);
    }

    public void setAdapter(RequestAdapter<T, SimpleResponse> adapter) {
        if (adapter == null)
            throw new IllegalArgumentException("request adapter cannot be null");
        this.adapter = adapter;
    }

}
