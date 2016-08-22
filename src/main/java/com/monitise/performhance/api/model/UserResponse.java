package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.User;

public class UserResponse {
    private int id;
    private int organizationId;
    private String name;
    private String surname;
    private String organizationName;
    private String jobTitle;
    private String teamName;
    private Role role;

    public UserResponse(User user) {
        id = user.getId();
        organizationName = user.getOrganization().getName();
        organizationId = user.getOrganization().getId();
        name = user.getName();
        surname = user.getSurname();
        role = user.getRole();
        if (user.getTeam() != null) {
            teamName = user.getTeam().getName();
        }
        if (user.getJobTitle() != null) {
            jobTitle = user.getJobTitle().getTitle();
        }
    }

    public static UserResponse fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new UserResponse(user);
    }

    // region Getters & Setters

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // endregion
}
