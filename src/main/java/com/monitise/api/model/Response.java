package com.monitise.entity;

public class Response<T> {

    private boolean success;
    private T data;
    private Error error;

    // region Getters

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public Error getError() {
        return error;
    }

    // endregion

    // region Setters

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(Error error) {
        this.error = error;
    }

    // endregion

}