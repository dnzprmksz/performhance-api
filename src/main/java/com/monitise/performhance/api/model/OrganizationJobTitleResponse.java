package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.JobTitle;

import java.util.ArrayList;
import java.util.List;

public class OrganizationJobTitleResponse {

    private int id;
    private String title;

    public OrganizationJobTitleResponse(JobTitle jobTitle) {
        id = jobTitle.getId();
        title = jobTitle.getTitle();
    }

    public static List<OrganizationJobTitleResponse> fromList(List<JobTitle> list) {
        if (list == null) {
            return null;
        }
        List<OrganizationJobTitleResponse> responseList = new ArrayList<>();
        for (JobTitle jobTitle : list) {
            responseList.add(new OrganizationJobTitleResponse(jobTitle));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // endregion

}
