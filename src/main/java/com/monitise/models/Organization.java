package com.monitise.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.List;

@Entity
public class Organization implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int numberOfEmployees;
    private List<Person> employees;
    private List<JobTitle> jobTitles;

    protected Organization() {}

    public Organization(String name) {
        this.name = name;
    }

    // region Getters

    public String getName() {
        return name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }

    // endregion

    // region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    // endregion

}