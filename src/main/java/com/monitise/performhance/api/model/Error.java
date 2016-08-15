package com.monitise.performhance.api.model;

public class Error {

    private int code;
    private String message;

    public Error(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // region Getters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    // endregion

    // region Setters

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // endregion

}