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
    private List<UserResponse> users;
    private List<JobTitle> jobTitles;
    private List<TeamResponse>  teams;

    public OrganizationResponse(Organization organization) {

        name = organization.getName();
        numberOfEmployees = organization.getNumberOfEmployees();
        users = UserResponse.fromUserList(organization.getUsers());
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

}
