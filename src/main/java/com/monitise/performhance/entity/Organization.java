package com.monitise.performhance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Organization {

    @Id
    @GeneratedValue
    private int id;
    @Column(unique = true)
    private String name;
    private int numberOfEmployees;
    @OneToMany
    @JsonIgnore
    private List<User> users;
    @OneToMany
    private List<JobTitle> jobTitles;
    @OneToMany
    private List<Team> teams;
    @OneToOne
    @JsonIgnore
    private User manager;

    protected Organization() {
    }

    public Organization(String name) {
        this.name = name;
    }

    public Organization(int id) {
        this.id = id;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public List<User> getUsers() {
        return users;
    }
    // endregion

    // region Setters

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
    // endregion

}