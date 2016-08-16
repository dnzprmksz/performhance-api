package com.monitise.performhance.api;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.AddUserRequest;
import com.monitise.performhance.api.model.CriteriaResponse;
import com.monitise.performhance.api.model.CriteriaUserResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.SimplifiedUser;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.JobTitleService;
import com.monitise.performhance.services.OrganizationService;
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

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    // region Dependencies

    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private RelationshipHelper relationshipHelper;
    @Autowired
    private CriteriaService criteriaService;

    // endregion

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<SimplifiedUser> addUser(@RequestBody AddUserRequest userRequest) throws BaseException {
        int organizationId = userRequest.getOrganizationId();
        securityHelper.checkAuthentication(organizationId);
        Organization organization = organizationService.get(organizationId);
        validateUserRequest(organization, userRequest);
        JobTitle title = jobTitleService.get(userRequest.getJobTitleId());
        // TODO: change the way username & password are set.
        User employee = new User(userRequest, organization,
                userRequest.getName() + "." + userRequest.getSurname(), "password");
        employee.setJobTitle(title);
        User addedEmployee = userService.addEmployee(employee);
        organizationService.addEmployee(organization, addedEmployee);

        SimplifiedUser responseEmployee = SimplifiedUser.fromUser(addedEmployee);
        Response<SimplifiedUser> response = new Response<>();
        response.setSuccess(true);
        response.setData(responseEmployee);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Response<SimplifiedUser> getSingleUser(@PathVariable int userId) throws BaseException {
        User user = userService.get(userId);
        int organizationId = user.getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        SimplifiedUser responseUser = SimplifiedUser.fromUser(user);

        Response response = new Response();
        response.setSuccess(true);
        response.setData(responseUser);
        return response;
    }

    // TODO: remove user from org. as well
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public Response<Object> deleteUser(@PathVariable int userId) throws BaseException {
        User user = userService.get(userId);
        securityHelper.checkAuthentication(user.getOrganization().getId());

        userService.remove(userId);
        Response response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getUserCriteriaList(@PathVariable int userId) throws BaseException {
        User user = userService.get(userId);
        int organizationId = user.getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        List<Criteria> criteriaList = user.getCriteriaList();
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(criteriaList);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria/{criteriaId}",
            method = RequestMethod.POST)
    public Response<CriteriaUserResponse> assignCriteriaToUser(@PathVariable int userId,
                                                               @PathVariable int criteriaId) throws BaseException {
        int organizationId = userService.get(userId).getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        relationshipHelper.checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteria = criteriaService.get(criteriaId);
        User userFromService = criteriaService.assignCriteriaToUserById(criteria, userId);
        CriteriaUserResponse criteriaUserResponse = new CriteriaUserResponse(userFromService);

        Response<CriteriaUserResponse> response = new Response<>();
        response.setData(criteriaUserResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria/{criteriaId}",
            method = RequestMethod.DELETE)
    public Response<Object> removeCriteriaFromUser(@PathVariable int userId,
                                                   @PathVariable int criteriaId) throws BaseException {
        int organizationId = userService.get(userId).getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        relationshipHelper.checkOrganizationCriteriaRelationship(organizationId, criteriaId);

        Criteria criteria = criteriaService.get(criteriaId);
        criteriaService.removeCriteriaFromUserById(criteria, userId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response<List<SimplifiedUser>> searchUsers(
            @RequestParam(value = "titleId", required = false, defaultValue = UserService.UNDEFINED) String titleId,
            @RequestParam(value = "teamId", required = false, defaultValue = UserService.UNDEFINED) String teamId,
            @RequestParam(value = "name", required = false, defaultValue = UserService.UNDEFINED) String name,
            @RequestParam(value = "surname", required = false, defaultValue = UserService.UNDEFINED) String surname)
            throws BaseException {

        if (UserService.UNDEFINED.equals(titleId) && UserService.UNDEFINED.equals(teamId)
                && UserService.UNDEFINED.equals(name) && UserService.UNDEFINED.equals(surname)) {
            throw new BaseException(ResponseCode.SEARCH_MISSING_PARAMETERS,
                    "At least one of titleId, teamId, name or surname parameters must be specified.");
        }

        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = manager.getOrganization();
        int organizationId = organization.getId();
        formatValidateSearchRequest(titleId, teamId);
        semanticallyValidate(organization, titleId, teamId);
        List<User> userList = userService.searchUsers(organizationId, teamId, titleId, name, surname);

        List<SimplifiedUser> simpleList = SimplifiedUser.fromUserList(userList);
        Response response = new Response();
        response.setData(simpleList);
        response.setSuccess(true);

        return response;
    }

    // region Helper Methods

    private void validateUserRequest(Organization organization, AddUserRequest employee) throws BaseException {
        String name = employee.getName();
        String surname = employee.getSurname();
        if (name == null || name.trim().equals("") || surname == null || surname.trim().equals("")) {
            throw new BaseException(ResponseCode.USER_USERNAME_NOT_EXIST, "Empty user name is not allowed.");
        }

        int titleId = employee.getJobTitleId();
        if (!organizationService.isJobTitleDefined(organization, titleId)) {
            throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST,
                    "A job title with given ID does not exist in this organization.");
        }
    }

    private void formatValidateSearchRequest(String titleId, String teamId) throws BaseException {
        if ((!titleId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(titleId))) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID,
                    "titleId must be positive integers");
        }

        if ((!teamId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(teamId))) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID,
                    "teamId must be positive integers");
        }

    }

    private void semanticallyValidate(Organization organization, String titleId, String teamId) throws BaseException {
        // Check if the title is defined in the organization.
        if (!titleId.equals(UserService.UNDEFINED)) {
            int intTitleId = Integer.parseInt(titleId);
            if (!organizationService.isJobTitleDefined(organization, intTitleId)) {
                throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST,
                        "Given job title id is not existent in the organization");
            }
        }

        // Check if the team is defined in the organization.
        if (!teamId.equals(UserService.UNDEFINED)) {
            int intTeamId = Integer.parseInt(teamId);
            if (!organizationService.isTeamIdDefined(organization, intTeamId)) {
                throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST,
                        "Given team's id is not existent in the organization");
            }
        }
    }

    private boolean isNonNegativeInteger(String str) throws BaseException {
        int candidate;
        try {
            candidate = Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID_FORMAT, "id's must be positive integers");
        }
        return candidate > 0;
    }

    // endregion

}
