package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.User;

import java.util.List;

public class CriteriaUserResponse {

    private int id;
    private String name;
    private String surname;
    private JobTitleResponse jobTitle;
    private List<CriteriaResponse> criteriaList;

    public CriteriaUserResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        jobTitle = JobTitleResponse.fromJobTitle(user.getJobTitle());
        criteriaList = CriteriaResponse.fromList(user.getCriteriaList());
    }

    public static CriteriaUserResponse fromUser(User user) {
        if (user == null) {
            return null;
        }
        return new CriteriaUserResponse(user);
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

    public JobTitleResponse getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitleResponse jobTitle) {
        this.jobTitle = jobTitle;
    }

    public List<CriteriaResponse> getCriteriaList() {
        return criteriaList;
    }

    public void setCriteriaList(List<CriteriaResponse> criteriaList) {
        this.criteriaList = criteriaList;
    }

    // endregion

}