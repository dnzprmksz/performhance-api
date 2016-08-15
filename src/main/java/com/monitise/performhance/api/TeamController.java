package com.monitise.performhance.api;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.SimplifiedTeamResponse;
import com.monitise.performhance.api.model.TeamResponse;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.TeamService;
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
    public Response<List<SimplifiedTeamResponse>> getAll() {
        List<Team> list = teamService.getAll();
        List<SimplifiedTeamResponse> responseList = SimplifiedTeamResponse.fromTeamList(list);

        Response<List<SimplifiedTeamResponse>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/", method = RequestMethod.GET)
    public Response<List<SimplifiedTeamResponse>> getTeamListByOrganizationId(@PathVariable int organizationId)
            throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Team> list = teamService.getListFilterByOrganizationId(organizationId);
        List<SimplifiedTeamResponse> responseList = SimplifiedTeamResponse.fromTeamList(list);

        Response<List<SimplifiedTeamResponse>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/{teamId}", method = RequestMethod.GET)
    public Response<TeamResponse> getTeamByOrganizationId(@PathVariable int organizationId,
                                                          @PathVariable int teamId) throws BaseException {
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
    public Response<TeamResponse> addTeam(@PathVariable int organizationId, @RequestBody String teamName)
            throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Organization organization = organizationService.get(organizationId);
        Team team = new Team(teamName, organization);
        Team addedTeam = teamService.add(team);
        organizationService.addTeam(organization, addedTeam);

        TeamResponse responseTeam = TeamResponse.fromTeam(addedTeam);

        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }

}