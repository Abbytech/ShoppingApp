package com.abbytech.util.eventform;

public interface ResponderEventParty<S, R> {
    void onRequestEvent(S event);

    void respond(R event);
}
