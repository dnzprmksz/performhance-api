package com.monitise.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.util.List;

@Entity
public class Organization implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;
    private int numberOfEmployees;
    @OneToMany
    private List<Employee> employees;
    @OneToMany
    private List<JobTitle> jobTitles;
    @OneToOne
    @JsonIgnore
    private Manager manager;

    protected Organization() {}

    public Organization(String name) {
        this.name = name;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }

    public Manager getManager() {
        return manager;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    // endregion

}