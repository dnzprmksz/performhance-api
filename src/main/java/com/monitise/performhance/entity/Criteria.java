package com.monitise.performhance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Criteria {

    @Id
    @GeneratedValue
    private int id;
    private String criteria;
    @ManyToOne
    private Organization organization;

    protected Criteria() {}

    public Criteria(String criteria, Organization organization) {
        this.criteria = criteria;
        this.organization = organization;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getCriteria() {
        return criteria;
    }

    public Organization getOrganization() {
        return organization;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // endregion

}