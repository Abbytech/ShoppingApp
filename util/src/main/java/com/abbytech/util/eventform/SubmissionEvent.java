package com.abbytech.util.eventform;

public class SubmissionEvent<T> {
    T extra;
    public SubmissionEvent(T extra) {
        this.extra = extra;
    }

    public T getExtra() {
        return extra;
    }
}
