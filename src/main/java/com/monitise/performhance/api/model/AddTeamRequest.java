package com.monitise.performhance.api.model;

public class AddTeamRequest {

    private String name;
    private int organizationId;

    public AddTeamRequest() {
    }

    // region Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    // endregion

}