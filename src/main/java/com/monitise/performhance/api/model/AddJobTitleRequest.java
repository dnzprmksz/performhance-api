package com.monitise.performhance.api.model;

public class AddJobTitleRequest {

    private String title;
    private int organizationId;

    public AddJobTitleRequest() {
    }

    // region Getters & Setters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    // endregion

}
