package com.abbytech.shoppingapp.notification;


import com.abbytech.shoppingapp.beacon.RegionStatus;

public class NotificationData {
    private String title;
    private String body;

    private RegionStatus status;

    NotificationData(String title, String body, RegionStatus status) {
        this.title = title;
        this.body = body;
        this.status = status;
    }
    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public RegionStatus getStatus() {
        return status;
    }

    @Override
    public int hashCode() {
        return status.hashCode();
    }
}
