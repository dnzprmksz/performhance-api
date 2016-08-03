package com.monitise.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity
public class Employee implements Serializable {

    @Id
    @GeneratedValue
    protected int id;
    protected String name;
    protected String surname;
    @ManyToOne
    protected JobTitle jobTitle;

    protected Employee() {}

    // region Getters

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    // endregion

    // region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    // endregion

}