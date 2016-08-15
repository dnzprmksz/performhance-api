package com.monitise.performhance.api.model;

public class Response<T> {

    protected boolean success;
    protected T data;
    protected Error error;

    // region Getters

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    // endregion

    // region Setters

    public void setData(T data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    // endregion

}