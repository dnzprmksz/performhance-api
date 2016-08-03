package com.monitise.models;

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


}