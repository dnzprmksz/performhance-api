package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.entity.Team;
import com.monitise.entity.User;

import java.util.List;

public class OrganizationResponse {

    private String name;
    private int numberOfEmployees;
    private List<User> users;
    private List<JobTitle> jobTitles;
    private List<Team>  teams;

    public OrganizationResponse(Organization organization) {

        name = organization.getName();
        numberOfEmployees = organization.getNumberOfEmployees();
        users = organization.getUsers();
        jobTitles = organization.getJobTitles();
        teams = organization.getTeams();
    }

}
