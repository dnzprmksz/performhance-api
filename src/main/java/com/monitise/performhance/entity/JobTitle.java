package com.monitise.performhance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class JobTitle {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    @ManyToOne
    private Organization organization;

    protected JobTitle() {
    }

    public JobTitle(String title, Organization organization) {
        this.title = title;
        this.organization = organization;
    }

    // region Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // endregion

}