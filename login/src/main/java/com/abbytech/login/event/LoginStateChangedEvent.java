package com.abbytech.login.event;

/**
 * Event-bus event that's sent when the login-service replies.
 */
public class LoginStateChangedEvent {
    private boolean loggedIn;
    private Object account;

    public LoginStateChangedEvent(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public Object getAccount() {
        return account;
    }

    public void setAccount(Object account) {
        this.account = account;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
