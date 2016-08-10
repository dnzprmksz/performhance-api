package com.monitise.api;

import com.monitise.api.model.CriteriaRequest;
import com.monitise.api.model.CriteriaResponse;
import com.monitise.api.model.CriteriaUserResponse;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.Error;
import com.monitise.api.model.Response;
import com.monitise.api.model.ResponseCode;
import com.monitise.entity.Criteria;
import com.monitise.entity.Team;
import com.monitise.entity.User;
import com.monitise.helpers.SecurityHelper;
import com.monitise.services.CriteriaService;
import com.monitise.services.OrganizationService;
import com.monitise.services.TeamService;
import com.monitise.services.UserService;
import org.junit.Test;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CriteriaController {

    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getAll() {
        List<Criteria> list = criteriaService.getAll();
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(list);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/criteria/{criteriaId}", method = RequestMethod.GET)
    public Response<CriteriaResponse> get(@PathVariable int criteriaId) throws BaseException {
        Criteria criteriaFromService = criteriaService.get(criteriaId);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getAllFilterByOrganizationId(@PathVariable int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(organizationId);
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(list);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/", method = RequestMethod.POST)
    public Response<CriteriaResponse> add(@PathVariable int organizationId, @RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteria = new Criteria(criteriaRequest.getCriteria(), organizationService.get(organizationId));
        Criteria criteriaFromService = criteriaService.add(criteria);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.GET)
    public Response<CriteriaResponse> get(@PathVariable int organizationId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteriaFromService = criteriaService.get(criteriaId);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToGroup(@PathVariable int organizationId, @PathVariable int criteriaId, @RequestBody List<Integer> userIdList) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        checkOrganizationUserListRelationship(organizationId, userIdList);

        ArrayList<Integer> existingUserList = new ArrayList<>();
        existingUserList = assignCriteriaToUserList(criteriaId, userIdList);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        if (!existingUserList.isEmpty()) {
            String message = "Completed successfully, however, the criteria was already assigned for the following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
            response.setError(new Error(ResponseCode.CRITERIA_EXISTS_IN_SOME_USERS, message));
        }
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.PUT)
    public Response<CriteriaResponse> update(@PathVariable int organizationId, @PathVariable int criteriaId, @RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteriaFromService = criteriaService.get(criteriaId);
        criteriaFromService.setCriteria(criteriaRequest.getCriteria());
        criteriaFromService = criteriaService.update(criteriaFromService);

        Response<CriteriaResponse> response = new Response<>();
        response.setData(CriteriaResponse.fromCriteria(criteriaFromService));
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int organizationId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        criteriaService.remove(criteriaId);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getUserCriteriaList(@PathVariable int organizationId, @PathVariable int userId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationUserRelationship(organizationId, userId);

        User user = userService.get(userId);
        List<Criteria> criteriaList = user.getCriteriaList();
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(criteriaList);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<CriteriaUserResponse> assignCriteriaToUser(@PathVariable int organizationId, @PathVariable int userId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationUserRelationship(organizationId, userId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteria = criteriaService.get(criteriaId);
        User userFromService = criteriaService.assignCriteriaToUserById(criteria, userId);
        CriteriaUserResponse criteriaUserResponse = new CriteriaUserResponse(userFromService);

        Response<CriteriaUserResponse> response = new Response<>();
        response.setData(criteriaUserResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> removeCriteriaFromUser(@PathVariable int organizationId, @PathVariable int userId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationUserRelationship(organizationId, userId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteria = criteriaService.get(criteriaId);
        criteriaService.removeCriteriaFromUserByID(criteria, userId);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/teams/{teamId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToTeamUsers(@PathVariable int organizationId, @PathVariable int teamId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationTeamRelationship(organizationId, teamId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        List<Integer> userIdList = userService.getIdListByTeamId(teamId);
        checkOrganizationUserListRelationship(organizationId, userIdList);

        ArrayList<Integer> existingUserList = new ArrayList<>();
        existingUserList = assignCriteriaToUserList(criteriaId, userIdList);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        if (!existingUserList.isEmpty()) {
            String message = "Completed successfully, however, the criteria was already assigned for the following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
            response.setError(new Error(ResponseCode.CRITERIA_EXISTS_IN_SOME_USERS, message));
        }
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/jobTitles/{jobTitleId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToJobTitleUsers(@PathVariable int organizationId, @PathVariable int jobTitleId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        checkOrganizationJobTitleRelationship(organizationId, jobTitleId);
        checkOrganizationCriteriaRelationship(organizationId, criteriaId);
        List<Integer> userIdList = userService.getIdListByJobTitleId(jobTitleId);
        checkOrganizationUserListRelationship(organizationId, userIdList);

        ArrayList<Integer> existingUserList = new ArrayList<>();
        existingUserList = assignCriteriaToUserList(criteriaId, userIdList);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        if (!existingUserList.isEmpty()) {
            String message = "Completed successfully, however, the criteria was already assigned for the following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
            response.setError(new Error(ResponseCode.CRITERIA_EXISTS_IN_SOME_USERS, message));
        }
        return response;
    }

    // region Helper Methods

    /**
     * @return The existence of the user.
     */
    private boolean checkUserExistenceAndAssignCriteriaById(int userId, int criteriaId) throws BaseException {
        Criteria criteria = criteriaService.get(criteriaId);
        try {
            criteriaService.assignCriteriaToUserById(criteria, userId);
        } catch (BaseException exception) {
            if (exception.getCode() == ResponseCode.CRITERIA_EXISTS_IN_USER) {
                return true;
            } else {
                throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given criteria to given user list.");
            }
        }
        return false;
    }


    /**
     * @return List of users who already has the criteria.
     */
    private ArrayList<Integer> assignCriteriaToUserList(int criteriaId, List<Integer> userIdList) throws BaseException {
        ArrayList<Integer> existingUserList = new ArrayList<>();
        // Add criteria to users. If user already has this criteria, add his/her ID to the list.
        for (int userId : userIdList) {
            boolean userAlreadyHasCriteria = checkUserExistenceAndAssignCriteriaById(userId, criteriaId);
            if (userAlreadyHasCriteria) {
                existingUserList.add(userId);
            }
        }
        return existingUserList;
    }

    private void checkOrganizationJobTitleRelationship(int organizationId, int jobTitleId) throws BaseException {
        if (!organizationService.isJobTitleDefined(organizationService.get(organizationId), jobTitleId)) {
            throw new BaseException(ResponseCode.JOB_TITLE_BELONGS_TO_ANOTHER_ORGANIZATION, "Given job title does not belong to this organization.");
        }
    }

    private void checkOrganizationCriteriaRelationship(int organizationId, int criteriaId) throws BaseException {
        Criteria criteria = criteriaService.get(criteriaId);
        int id = criteria.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.CRITERIA_BELONGS_TO_ANOTHER_ORGANIZATION, "Given criteria does not belong to this organization.");
        }
    }

    private void checkOrganizationTeamRelationship(int organizationId, int teamId) throws BaseException {
        Team team = teamService.get(teamId);
        int id = team.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.TEAM_BELONGS_TO_ANOTHER_ORGANIZATION, "Given team does not belong to this organization.");
        }
    }

    private void checkOrganizationUserRelationship(int organizationId, int userId) throws BaseException {
        User user = userService.get(userId);
        int id = user.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.USER_BELONGS_TO_ANOTHER_ORGANIZATION, "Given user does not belong to this organization.");
        }
    }

    private void checkOrganizationUserListRelationship(int organizationId, List<Integer> userIdList) throws BaseException {
        for (int userId : userIdList) {
            checkOrganizationUserRelationship(organizationId, userId);
        }
    }

    // endregion

}