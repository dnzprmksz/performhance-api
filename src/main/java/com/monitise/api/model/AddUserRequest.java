package com.monitise.api.model;

import java.io.Serializable;

public class AddUserRequest implements Serializable{
    private String name;
    private String surname;
    private int jobTitleId;

    public AddUserRequest() {}

    // region Getters

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getJobTitleId() {
        return jobTitleId;
    }

    // endregion

    // region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    // endregion
}
