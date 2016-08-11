package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.User;

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

    public int getOrganizationId() {
        return organizationId;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public Role getRole() {
        return role;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle.getTitle();
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // endregion
}