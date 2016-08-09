package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedUser {
    private int id;
    private String name;
    private String surname;
    private JobTitle jobTitle;
    private Role role;

    public SimplifiedUser(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        jobTitle = user.getJobTitle();
        role = user.getRole();
    }


    public static SimplifiedUser fromUser(User user) {
        return new SimplifiedUser(user);
    }

    public static List<SimplifiedUser> fromUserList(List<User> users) {
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

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public JobTitle getJobTitle() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    // endregion
}