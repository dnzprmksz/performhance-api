package com.monitise.performhance.api;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.AddTeamRequest;
import com.monitise.performhance.api.model.ExtendedResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.SimplifiedTeam;
import com.monitise.performhance.api.model.SimplifiedUser;
import com.monitise.performhance.api.model.TeamResponse;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
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

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<SimplifiedTeam>> getAll() {
        List<Team> list = teamService.getAll();
        List<SimplifiedTeam> responseList = SimplifiedTeam.fromList(list);

        Response<List<SimplifiedTeam>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<TeamResponse> addTeam(@RequestBody AddTeamRequest addTeamRequest)
            throws BaseException {
        int organizationId = addTeamRequest.getOrganizationId();
        securityHelper.checkAuthentication(organizationId);

        Organization organization = organizationService.get(organizationId);
        Team team = new Team(addTeamRequest.getName(), organization);
        Team addedTeam = teamService.add(team);
        organizationService.addTeam(organization, addedTeam);

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
        int organizationId = team.getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        TeamResponse responseTeam = TeamResponse.fromTeam(team);

        Response<TeamResponse> response = new Response<>();
        response.setData(responseTeam);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}", method = RequestMethod.POST)
    public Response<TeamResponse> assignSingleEmployee(@PathVariable int teamId, @RequestBody int userId)
            throws BaseException {
        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = manager.getOrganization();
        int organizationId = organization.getId();
        securityHelper.checkAuthentication(organizationId);

        validateAssignmentRequest(organizationId, teamId, userId);
        Team team = teamService.get(teamId);
        User employee = userService.get(userId);
        Team updatedTeam = teamService.assignEmployeeToTeam(employee, team);

        TeamResponse teamResponse = TeamResponse.fromTeam(updatedTeam);
        Response<TeamResponse> response = new Response<>();
        response.setData(teamResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{teamId}/criteria/{criteriaId}",
            method = RequestMethod.POST)
    public Response<Object> assignCriteriaToTeamUsers(@PathVariable int teamId,
                                                      @PathVariable int criteriaId) throws BaseException {
        int organizationId = teamService.get(teamId).getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        relationshipHelper.checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        List<Integer> userIdList = userService.getIdListByTeamId(teamId);
        relationshipHelper.checkOrganizationUserListRelationship(organizationId, userIdList);

        ArrayList<Integer> existingUserList = new ArrayList<>();
        existingUserList = criteriaService.assignCriteriaToUserList(criteriaId, userIdList);

        ExtendedResponse<Object> response = new ExtendedResponse<>();
        response.setSuccess(true);
        if (!existingUserList.isEmpty()) {
            String message = "Completed successfully, however, the criteria was already assigned for following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
            response.setMessage(message);
        }
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response<List<SimplifiedUser>> searchUsers(
            @RequestParam(value = "teamId", required = false, defaultValue = TeamService.UNDEFINED) String teamId,
            @RequestParam(value = "teamName", required = false, defaultValue = TeamService.UNDEFINED) String teamName)
            throws BaseException {
        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = manager.getOrganization();
        int organizationId = organization.getId();
        securityHelper.checkAuthentication(organizationId);

        if (teamName.equals(UserService.UNDEFINED) && teamId.equals(UserService.UNDEFINED)) {
            throw new BaseException(ResponseCode.SEARCH_MISSING_PARAMETERS,
                    "At least one of teamId or teamName parameters must be specified.");
        }

        List<Team> teamList = teamService.searchTeams(organizationId, teamId, teamName);
        List<TeamResponse> teamResponseList = TeamResponse.fromTeamList(teamList);
        Response response = new Response();
        response.setData(teamResponseList);
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void validateAssignmentRequest(int organizationId, int teamId, int userId) throws BaseException {
        relationshipHelper.checkOrganizationTeamRelationship(organizationId, teamId);
        relationshipHelper.checkOrganizationUserRelationship(organizationId, userId);
    }

    // endregion

}