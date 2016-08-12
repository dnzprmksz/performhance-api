package com.monitise.api;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.monitise.api.model.AddUserRequest;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.ResponseCode;
import com.monitise.api.model.SimplifiedUser;
import com.monitise.api.model.TeamUserResponse;
import com.monitise.helpers.SecurityHelper;
import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.api.model.Response;
import com.monitise.api.model.Role;
import com.monitise.entity.User;
import com.monitise.repositories.UserRepository;
import com.monitise.services.JobTitleService;
import com.monitise.services.OrganizationService;
import com.monitise.services.UserService;
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
public class UserController {

    @Autowired
    SecurityHelper securityHelper;
    @Autowired
    UserService userService;
    @Autowired
    OrganizationService organizationService;
    @Autowired
    JobTitleService jobTitleService;


    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users", method = RequestMethod.GET)
    public Response<List<TeamUserResponse>> getUsers(@PathVariable int organizationId ) throws BaseException {
        checkAuthentication(organizationId);
        List<User> users = userService.getByOrganizationId(organizationId);

        List<TeamUserResponse> responseList = TeamUserResponse.fromUserList(users);
        Response response = new Response();
        response.setSuccess(true);
        response.setData(responseList);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}", method = RequestMethod.GET)
    public Response<SimplifiedUser> getSingleUser(@PathVariable int organizationId,
                                        @PathVariable int userId )throws BaseException {
        checkAuthentication(organizationId);
        User user = userService.get(userId);
        SimplifiedUser responseUser = SimplifiedUser.fromUser(user);

        Response response = new Response();
        response.setSuccess(true);
        response.setData(responseUser);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users", method = RequestMethod.POST)

    public Response<SimplifiedUser> addUser(@PathVariable int organizationId,
                                            @RequestBody AddUserRequest userRequest) throws BaseException {
        checkAuthentication(organizationId);
        Organization organization = organizationService.get(organizationId);
        validateUserRequest(organization, userRequest);
        JobTitle title = jobTitleService.get(userRequest.getJobTitleId());
        // TODO: change the way username & password are set.
        User employee = new User(userRequest,organization,
                userRequest.getName()+"."+userRequest.getSurname(),"password");
        employee.setJobTitle(title);
        User addedEmployee = userService.addEmployee(employee);
        organizationService.addEmployee(organization,addedEmployee);

        SimplifiedUser responseEmployee = SimplifiedUser.fromUser(addedEmployee);
        Response<SimplifiedUser> response = new Response<>();
        response.setSuccess(true);
        response.setData(responseEmployee);
        return response;
    }

    // TODO: remove user from org. as well
    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users/", method = RequestMethod.DELETE)
    public Response<SimplifiedUser> deleteUser(@PathVariable int organizationId,
                                     @PathVariable int userId) throws BaseException {
        checkAuthentication(organizationId);
        User soonToBeDeleted = userService.get(userId);
        if(soonToBeDeleted.getOrganization().getId() != organizationId) {
            throw new BaseException(ResponseCode.USER_UNAUTHORIZED_ORGANIZATION, "You are not authorized to perform this action.");
        }
        userService.remove(userId);
        SimplifiedUser responseUser = SimplifiedUser.fromUser(soonToBeDeleted);
        Response response = new Response<>();
        response.setData(responseUser);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/users/search", method = RequestMethod.GET)
    public Response< List<SimplifiedUser> > searchUsers(
            @RequestParam(value = "titleId", required = false, defaultValue = UserService.UNDEFINED) String titleId,
            @RequestParam(value = "teamId", required = false, defaultValue = UserService.UNDEFINED) String teamId) throws BaseException {

        if (titleId.equals(UserService.UNDEFINED) && teamId.equals(UserService.UNDEFINED)) {
            throw new BaseException(ResponseCode.UNEXPECTED,
                    "At least one of titleId and teamId must be specified.");
        }

        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = manager.getOrganization();
        int organizationId = organization.getId();
        formatValidateSearchRequest(titleId, teamId);
        semanticallyValidate(organization, titleId, teamId);
        List<User> userList = userService.searchUsers(organizationId, teamId, titleId);
        
        List<SimplifiedUser> simpleList = SimplifiedUser.fromUserList(userList);
        Response response = new Response();
        response.setData(simpleList);
        response.setSuccess(true);

        return response;
    }



    // region Helper Methods

    private void validateUserRequest(Organization organization, AddUserRequest employee) throws BaseException{
        String name = employee.getName();
        String surname = employee.getSurname();
        if (name == null || name.trim().equals("") || surname == null || surname.trim().equals("")) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty user name is not allowed.");
        }

        int titleId = employee.getJobTitleId();
        if(!organizationService.isJobTitleDefined(organization, titleId)) {
            throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST, "A job title with given ID does not exist in this organization.");
        }
    }

    private void checkAuthentication(int organizationId) throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        User currentUser = securityHelper.getAuthenticatedUser();
        if (currentUser.getRole().equals(Role.MANAGER)) {
            securityHelper.checkUserOrganizationAuthorization(organizationId);
        }
    }

    private void formatValidateSearchRequest(String titleId, String teamId) throws BaseException {
        if( ( !titleId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(titleId)) ) {
            throw new BaseException(ResponseCode.UNEXPECTED,
                    "titleId must be positive integers");
        }

        if( ( !teamId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(teamId)) ) {
            throw new BaseException(ResponseCode.UNEXPECTED,
                    "teamId must be positive integers");
        }

    }

    private void semanticallyValidate(Organization organization, String titleId, String teamId) throws BaseException {
        // Check if the title is defined in the organization.
        if (!titleId.equals(UserService.UNDEFINED)) {
            int intTitleId = Integer.parseInt(titleId);
            if (!organizationService.isJobTitleDefined(organization,intTitleId)) {
               throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST,
                       "Given job title id is not existent in the organization");
            }
        }

        // Check if the team is defined in the organization.
        if (!teamId.equals(UserService.UNDEFINED)) {
            int intTeamId = Integer.parseInt(teamId);
            if (!organizationService.isTeamIdDefined(organization,intTeamId)) {
                throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST,
                        "Given team's id is not existent in the organization");
            }
        }
    }

    private boolean isNonNegativeInteger(String K) {
        int len = K.length();
        for(int i=0 ; i<len ; ++i){
            char cur = K.charAt(i);
            if( !('0'<=cur && cur<='9') ){
                return false;
            }
        }
        return true;
    }

    // endregion

}
