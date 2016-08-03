package com.monitise.models;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class Organization implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private int numberOfEmployees;
    @OneToMany
    private List<Employee> employees;
    @OneToMany
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

    public List<Employee> getEmployees() {
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

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    // endregion

}