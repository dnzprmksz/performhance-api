package com.monitise.performhance.api.model;

public class CriteriaRequest {

    private String criteria;
    private int organizationId;

    public CriteriaRequest() {
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

}
