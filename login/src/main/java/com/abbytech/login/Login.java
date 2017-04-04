package com.abbytech.login;


import com.abbytech.login.event.LoginStateChangedEvent;

public interface Login<T> {
    void registerLoginEventSubscriber(Object subscriber);

    /**
     * Called when an attempt has been made to login, event's extra should contain
     * the account that was attempted.
     *
     * @param event
     */
    void onLoginStateChanged(LoginStateChangedEvent event);

    /**
     * Attempt to login with provided account; result of this attempt should be conveyed
     * to listeners using onLoginStateChanged.
     *
     * @param account
     */
    void login(T account);

    void logout();
}
