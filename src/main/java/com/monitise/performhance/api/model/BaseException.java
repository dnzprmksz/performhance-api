package com.monitise.performhance.api.model;

public class BaseException extends Exception {

    private int code;
    private String message;

    public BaseException(int code, String message) {
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

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // endregion

}