package com.abbytech.login.data;

public class BasicLoginData {
    private String username;
    private String password;

    public BasicLoginData(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        if (username.isEmpty() || password.isEmpty())
            return "";
        else
            return String.format("username=%1s,password=%2s", username, password);
    }
}
