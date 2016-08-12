package com.monitise.performhance.api.model;

public class ExtendedResponse<T> extends Response<T> {

    private String message;

    public ExtendedResponse() {}

    public ExtendedResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}