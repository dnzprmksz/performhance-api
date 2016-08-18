package com.monitise.performhance.api;

import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.api.model.AddTeamRequest;
import com.monitise.performhance.api.model.ExtendedResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.SimplifiedTeam;
import com.monitise.performhance.api.model.SimplifiedUser;
import com.monitise.performhance.api.model.TeamResponse;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.TeamService;
import com.monitise.performhance.services.UserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    // region Dependencies

    @Autowired
    private TeamService teamService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipHelper relationshipHelper;
    @Autowired
    private CriteriaService criteriaService;

    // endregion

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<SimplifiedTeam>> getAll() throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        List<Team> list = teamService.getListFilterByOrganizationId(organizationId);

        List<SimplifiedTeam> responseList = SimplifiedTeam.fromList(list);
        Response<List<SimplifiedTeam>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<TeamResponse> addTeam(@RequestBody AddTeamRequest addTeamRequest) throws BaseException {
        int organizationId = addTeamRequest.getOrganizationId();
        securityHelper.checkAuthentication(organizationId);

        Organization organization = organizationService.get(organizationId);
        Team team = new Team(addTeamRequest.getName(), organization);
        Team addedTeam = teamService.add(team);

        TeamResponse responseTeam = TeamResponse.fromTeam(addedTeam);
        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}", method = RequestMethod.GET)
    public Response<TeamResponse> getTeam(@PathVariable int teamId) throws BaseException {
        Team team = teamService.get(teamId);
        securityHelper.checkAuthentication(team.getOrganization().getId());
        TeamResponse responseTeam = TeamResponse.fromTeam(team);

        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }
    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}/leader/{userId}", method = RequestMethod.POST)
    public Response<TeamResponse> assignTeamLeader(@PathVariable int teamId, @PathVariable int userId)
            throws BaseException {
        securityHelper.checkAuthentication(teamService.get(teamId).getOrganization().getId());
        securityHelper.checkAuthentication(userService.get(userId).getOrganization().getId());

        Team updatedTeam = teamService.assignLeaderToTeam(userId, teamId);
        TeamResponse teamResponse = TeamResponse.fromTeam(updatedTeam);
        Response<TeamResponse> response = new Response<>();
        response.setData(teamResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}/users/{userId}", method = RequestMethod.POST)
    public Response<TeamResponse> assignEmployee(@PathVariable int teamId, @PathVariable int userId)
            throws BaseException {
        securityHelper.checkAuthentication(teamService.get(teamId).getOrganization().getId());
        securityHelper.checkAuthentication(userService.get(userId).getOrganization().getId());

        Team updatedTeam = teamService.assignEmployeeToTeam(userId, teamId);
        TeamResponse teamResponse = TeamResponse.fromTeam(updatedTeam);
        Response<TeamResponse> response = new Response<>();
        response.setData(teamResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToTeamUsers(@PathVariable int teamId,
                                                      @PathVariable int criteriaId) throws BaseException {
        int organizationId = teamService.get(teamId).getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        relationshipHelper.checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        List<Integer> userIdList = userService.getIdListByTeamId(teamId);
        relationshipHelper.checkOrganizationUserListRelationship(organizationId, userIdList);

        ArrayList<Integer> existingUserList = criteriaService.assignCriteriaToUserList(criteriaId, userIdList);
        ExtendedResponse<Object> response = new ExtendedResponse<>();
        response.setMessage(generateExistingUsersMessage(existingUserList));
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response<List<SimplifiedUser>> searchUsers(
            @RequestParam(value = "teamName", required = false, defaultValue = TeamService.UNDEFINED) String teamName)
            throws BaseException {
        if (teamName.equals(UserService.UNDEFINED)) {
            throw new BaseException(ResponseCode.SEARCH_MISSING_PARAMETERS, "teamName parameter must be specified.");
        }
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        List<Team> teamList = teamService.searchTeams(organizationId, teamName);

        List<TeamResponse> teamResponseList = TeamResponse.fromTeamList(teamList);
        Response response = new Response();
        response.setData(teamResponseList);
        response.setSuccess(true);
        return response;
    }

    private String generateExistingUsersMessage(ArrayList<Integer> existingUserList) {
        String message = null;
        if (!existingUserList.isEmpty()) {
            message = "Completed successfully, however, the criteria was already assigned for following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
        }
        return message;
    }
}