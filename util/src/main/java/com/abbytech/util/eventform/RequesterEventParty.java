package com.abbytech.util.eventform;

public interface RequesterEventParty<S, R> {
    S createRequestEvent();
    void onSubmissionResponse(R event);
}
