package com.monitise.performhance.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.jpa.domain.Specification;

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

    protected Team() {
    }

    public Team(String name, Organization organization) {
        this.name = name;
        this.organization = organization;
    }

    public static Specification<Team> organizationIdIs(int organizationId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("organization"), organizationId);
        };
    }

    public static Specification<Team> teamIdIs(int teamId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("id"), teamId);
        };
    }

    public static Specification<Team> teamNameContains(String name) {
        return (root, query, cb) -> {
            return cb.like(root.<String>get("name"), "%" + name + "%");
        };
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

    public List<User> getMembers() {
        return members;
    }

    // endregion

    // region Setters

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    // endregion

}