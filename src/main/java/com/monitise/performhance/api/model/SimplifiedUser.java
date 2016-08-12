package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedUser {
    private int id;
    private int organizationId;
    private String name;
    private String surname;
    private String jobTitle;
    private Role role;

    public SimplifiedUser(User user) {
        id = user.getId();
        organizationId = user.getOrganization().getId();
        name = user.getName();
        surname = user.getSurname();
        role = user.getRole();
        if (user.getJobTitle() != null) {
            jobTitle = user.getJobTitle().getTitle();
        }
    }

    public static SimplifiedUser fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new SimplifiedUser(user);
    }

    public static List<SimplifiedUser> fromUserList(List<User> users) {
        if (users == null) {
            return null;
        }
        List<SimplifiedUser> responses = new ArrayList<>();
        for (User user : users) {
            SimplifiedUser current = fromUser(user);
            responses.add(current);
        }
        return responses;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // endregion

    // region Setters

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle.getTitle();
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // endregion
}