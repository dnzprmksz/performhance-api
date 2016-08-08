package com.monitise.controllers;

import com.monitise.helpers.SecurityHelper;
import com.monitise.models.BaseException;
import com.monitise.models.Response;
import com.monitise.models.Team;
import com.monitise.services.OrganizationService;
import com.monitise.services.TeamService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger LOGGER = LogManager.getLogger(TeamController.class);

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/teams/", method = RequestMethod.GET)
    public Response<List<Team>> getAll() {
        String log = "GET /teams";
        LOGGER.info(log);
        List<Team> list = teamService.getAll();

        Response<List<Team>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/", method = RequestMethod.GET)
    public Response<List<Team>> getTeamListByOrganizationId(@PathVariable int organizationId) throws BaseException {
        String log = "GET /organizations/" + organizationId + "/teams";
        LOGGER.info(log);
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Team> list = teamService.getListFilterByOrganizationId(organizationId);

        Response<List<Team>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/{teamId}", method = RequestMethod.GET)
    public Response<Team> getTeamByOrganizationId(@PathVariable int organizationId, @PathVariable int teamId) throws BaseException {
        String log = "GET /organizations/" + organizationId + "/teams/" + teamId;
        LOGGER.info(log);
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Team team = teamService.get(teamId);

        Response<Team> response = new Response<>();
        response.setData(team);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/", method = RequestMethod.POST)
    public Response<Team> addTeam(@PathVariable int organizationId, @RequestBody Team team) throws BaseException {
        String log = "POST /organizations/" + organizationId + "/teams - Team Name: " + team.getName();
        LOGGER.info(log);
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        team.setOrganization(organizationService.get(organizationId));
        Team teamFromService = teamService.add(team);

        Response<Team> response = new Response<>();
        response.setData(teamFromService);
        response.setSuccess(true);
        return response;
    }

}