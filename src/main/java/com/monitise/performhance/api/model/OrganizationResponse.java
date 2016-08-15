package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationResponse {

    private int id;
    private String name;
    private int numberOfEmployees;
    private List<SimplifiedUser> users;
    private List<JobTitle> jobTitles;
    private List<TeamResponse> teams;

    public OrganizationResponse(Organization organization) {
        id = organization.getId();
        name = organization.getName();
        numberOfEmployees = organization.getNumberOfEmployees();
        users = SimplifiedUser.fromUserList(organization.getUsers());
        jobTitles = organization.getJobTitles();
        teams = TeamResponse.fromTeamList(organization.getTeams());
    }

    public static OrganizationResponse fromOrganization(Organization organization) {
        if (organization == null) {
            return null;
        }
        return new OrganizationResponse(organization);
    }

    public static List<OrganizationResponse> fromOrganizationList(List<Organization> organizations) {
        if (organizations == null) {
            return null;
        }
        List<OrganizationResponse> responses = new ArrayList<>();
        for (Organization organization : organizations) {
            OrganizationResponse current = fromOrganization(organization);
            responses.add(current);
        }
        return responses;
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

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    // endregion

    // region Setters

    public List<SimplifiedUser> getUsers() {
        return users;
    }

    public void setUsers(List<SimplifiedUser> users) {
        this.users = users;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public List<TeamResponse> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamResponse> teams) {
        this.teams = teams;
    }

    // endregion
}
