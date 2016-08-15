package com.monitise.performhance.entity;

import com.monitise.performhance.api.model.AddUserRequest;
import com.monitise.performhance.api.model.Role;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class User {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String surname;
    @ManyToOne
    private JobTitle jobTitle;
    @Enumerated(EnumType.STRING)
    private Role role;
    @ManyToOne
    private Organization organization;
    @ManyToOne
    private Team team;
    @ManyToMany
    private List<Criteria> criteriaList;
    private String username;
    private String password;

    protected User() {
    }

    public User(String name, String surname, Organization organization) {
        this.name = name;
        this.surname = surname;
        this.organization = organization;
        role = Role.EMPLOYEE;
    }

    public User(String name, String surname, Organization organization, Role role) {
        this.name = name;
        this.surname = surname;
        this.organization = organization;
        this.role = role;
    }

    public User(AddUserRequest userRequest, Organization organization, String username, String password) {
        name = userRequest.getName();
        surname = userRequest.getSurname();
        this.organization = organization;
        this.password = password;
        this.username = username;
        role = Role.EMPLOYEE;
    }

    public static Specification<User> alwaysTrue() {
        return (root, query, cb) -> {
            return cb.greaterThanOrEqualTo(root.get("id"), -1);
        };
    }

    public static Specification<User> teamIdIs(int teamId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("team"), teamId);
        };
    }

    public static Specification<User> titleIdIs(int titleId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("jobTitle"), titleId);
        };
    }

    public static Specification<User> nameContains(String input) {
        return (root, query, cb) -> {
            return cb.like(root.<String>get("name"), "%" + input + "%");
        };
    }

    public static Specification<User> surnameContains(String input) {
        return (root, query, cb) -> {
            return cb.like(root.<String>get("surname"), "%" + input + "%");
        };
    }


    public static Specification<User> idMoreThan(int id) {
        return (root, query, cb) -> {
            return cb.greaterThan(root.get("id"), id);
        };
    }

    public static Specification<User> organizationIdIs(int organizationId) {
        return (root, query, cb) -> {
            return cb.equal(root.get("organization"), organizationId);
        };
    }


    // region Getters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // endregion

    // region Setters

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
    // endregion

}