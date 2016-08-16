package com.monitise.performhance.api.model;

import java.io.Serializable;

public class AddUserRequest implements Serializable {
    private String name;
    private String surname;
    private int jobTitleId;
    private int organizationId;

    public AddUserRequest() {
    }

    // region Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getJobTitleId() {
        return jobTitleId;
    }

    public void setJobTitleId(int jobTitleId) {
        this.jobTitleId = jobTitleId;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }


    // endregion
}
