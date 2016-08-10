package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.User;

import java.util.List;

public class CriteriaUserResponse {

    private int id;
    private String name;
    private String surname;
    private JobTitle jobTitle;
    private List<CriteriaResponse> criteriaList;

    public CriteriaUserResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        jobTitle = user.getJobTitle();
        criteriaList = CriteriaResponse.fromList(user.getCriteriaList());
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

    public List<CriteriaResponse> getCriteriaList() {
        return criteriaList;
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

    public void setCriteriaList(List<CriteriaResponse> criteriaList) {
        this.criteriaList = criteriaList;
    }

    // endregion

}