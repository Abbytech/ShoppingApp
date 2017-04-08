package com.abbytech.shoppingapp.notification;


public class NotificationData {
    private String title;
    private String body;

    public NotificationData(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }
}
