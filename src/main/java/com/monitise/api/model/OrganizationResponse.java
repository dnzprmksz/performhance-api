package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.entity.Team;
import com.monitise.entity.User;
import org.springframework.security.web.util.matcher.OrRequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class OrganizationResponse {

    private String name;
    private int numberOfEmployees;
    private List<SimplifiedUser> users;
    private List<JobTitle> jobTitles;
    private List<TeamResponse>  teams;

    public OrganizationResponse(Organization organization) {

        name = organization.getName();
        numberOfEmployees = organization.getNumberOfEmployees();
        users = SimplifiedUser.fromUserList(organization.getUsers());
        jobTitles = organization.getJobTitles();
        teams = TeamResponse.fromTeamList(organization.getTeams());
    }

    public static OrganizationResponse fromOrganization(Organization organization) {
        return new OrganizationResponse(organization);
    }

    public static List<OrganizationResponse> fromOrganizationList(List<Organization> organizations){
        List<OrganizationResponse> responses = new ArrayList<>();
        for(Organization organization : organizations){
            OrganizationResponse current = fromOrganization(organization);
            responses.add(current);
        }
        return responses;
    }

    // region Getters

    public String getName() {
        return name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public List<SimplifiedUser> getUsers() {
        return users;
    }

    public List<JobTitle> getJobTitles() {
        return jobTitles;
    }

    public List<TeamResponse> getTeams() {
        return teams;
    }

    // endregion

    // region Setters

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public void setUsers(List<SimplifiedUser> users) {
        this.users = users;
    }

    public void setJobTitles(List<JobTitle> jobTitles) {
        this.jobTitles = jobTitles;
    }

    public void setTeams(List<TeamResponse> teams) {
        this.teams = teams;
    }

    // endregion
}
