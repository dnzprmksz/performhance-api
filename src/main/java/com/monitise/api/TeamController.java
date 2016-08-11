package com.monitise.api;

import com.monitise.api.model.TeamResponse;
import com.monitise.entity.Organization;
import com.monitise.helpers.SecurityHelper;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.Response;
import com.monitise.entity.Team;
import com.monitise.services.OrganizationService;
import com.monitise.services.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TeamController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private OrganizationService organizationService;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teams/", method = RequestMethod.GET)
    public Response<List<TeamResponse>> getAll() {
        List<Team> list = teamService.getAll();
        List<TeamResponse> responseList = TeamResponse.fromTeamList(list);

        Response<List<TeamResponse>> response = new Response();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/", method = RequestMethod.GET)
    public Response<List<TeamResponse>> getTeamListByOrganizationId(@PathVariable int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Team> list = teamService.getListFilterByOrganizationId(organizationId);
        List<TeamResponse> responseList = TeamResponse.fromTeamList(list);

        Response<List<TeamResponse>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/{teamId}", method = RequestMethod.GET)
    public Response<TeamResponse> getTeamByOrganizationId(@PathVariable int organizationId, @PathVariable int teamId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Team team = teamService.get(teamId);
        TeamResponse responseTeam = TeamResponse.fromTeam(team);

        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/", method = RequestMethod.POST)
    public Response<TeamResponse> addTeam(@PathVariable int organizationId, @RequestBody String teamName) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Organization organization = organizationService.get(organizationId);
        Team team = new Team(teamName,organization);
        Team addedTeam = teamService.add(team);
        organizationService.addTeam(organization,addedTeam);

        TeamResponse responseTeam = TeamResponse.fromTeam(addedTeam);

        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }

}