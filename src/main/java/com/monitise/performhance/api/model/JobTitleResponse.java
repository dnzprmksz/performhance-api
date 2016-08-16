package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.JobTitle;

import java.util.ArrayList;
import java.util.List;

public class JobTitleResponse {

    private int id;
    private String title;
    private int organizationId;

    public JobTitleResponse(JobTitle jobTitle) {
        id = jobTitle.getId();
        title = jobTitle.getTitle();
        organizationId = jobTitle.getOrganization().getId();
    }

    public static List<JobTitleResponse> fromList(List<JobTitle> list) {
        List<JobTitleResponse> responseList = new ArrayList<>();
        for (JobTitle jobTitle : list) {
            responseList.add(new JobTitleResponse(jobTitle));
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

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    // endregion

}
