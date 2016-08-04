package com.monitise.models;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity
public class Manager extends Employee implements Serializable {

    @OneToOne
    private Organization organization;

    protected Manager() {}

    public Manager(Organization organization) {
        name = organization.getName();
        surname = "Manager";
        this.organization = organization;
    }

    public Manager(String name, String surname, Organization organization) {
        this.name = name;
        this.surname = surname;
        this.organization = organization;
    }

    // region Getters

    public Organization getOrganization() {
        return organization;
    }

    // endregion

    // region Setters

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // endregion
}