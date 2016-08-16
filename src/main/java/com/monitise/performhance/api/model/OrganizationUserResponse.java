package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.User;

import java.util.ArrayList;
import java.util.List;

public class OrganizationUserResponse {

    private int id;
    private String name;
    private String surname;
    private String jobTitle;
    private String teamName;

    public OrganizationUserResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        if (user.getJobTitle() != null) {
            jobTitle = user.getJobTitle().getTitle();
        }
        if (user.getTeam() != null) {
            teamName = user.getTeam().getName();
        }
    }

    public static List<OrganizationUserResponse> fromList(List<User> userList) {
        if (userList == null) {
            return null;
        }
        List<OrganizationUserResponse> responseList = new ArrayList<>();
        for (User user : userList) {
            responseList.add(new OrganizationUserResponse(user));
        }
        return responseList;
    }

    // region Getters & Setters

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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    // endregion

}
