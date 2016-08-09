package com.monitise.entity;

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

    @Override
    public String getMessage() {
        return message;
    }

    // endregion

    // region Setters

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    // endregion

}