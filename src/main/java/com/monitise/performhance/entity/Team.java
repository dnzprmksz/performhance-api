package com.monitise.performhance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany
    private List<User> members;
    @ManyToOne
    @JsonIgnore
    private Organization organization;
    @OneToOne
    private User leader;

    protected Team() {}

    public Team(String name, Organization organization) {
        this.name = name;
        this.organization = organization;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public User getLeader() {
        return leader;
    }

    public Organization getOrganization() {
        return organization;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // endregion

}