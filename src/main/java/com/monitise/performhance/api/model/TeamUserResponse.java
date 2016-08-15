package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TeamUserResponse {

    private int id;
    private String name;
    private String surname;
    private String jobTitle;

    public TeamUserResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        if (user.getJobTitle() != null) {
            jobTitle = user.getJobTitle().getTitle();
        }
    }

    public static List<TeamUserResponse> fromUserList(List<User> users) {
        if (users == null) {
            return null;
        }
        List<TeamUserResponse> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(new TeamUserResponse(user));
        }
        return responseList;
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

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // endregion

}