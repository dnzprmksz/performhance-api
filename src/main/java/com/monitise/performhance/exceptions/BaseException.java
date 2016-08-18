package com.monitise.performhance.exceptions;

public class BaseException extends Exception {

    protected int code;
    protected String message;

    public BaseException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // region Getters & Setters

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // endregion

}