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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    // endregion

}