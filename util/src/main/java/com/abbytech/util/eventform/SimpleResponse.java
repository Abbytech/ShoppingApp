package com.abbytech.util.eventform;

public class SimpleResponse {

    boolean success;
    String message;

    Throwable errorThrowable;

    public SimpleResponse(boolean success, String message, Throwable errorThrowable) {
        this(success, message);
        this.errorThrowable = errorThrowable;
    }

    public SimpleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccessful() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Throwable getError() {
        return errorThrowable;
    }
}
