package com.monitise.api;

import com.monitise.api.model.BaseException;
import com.monitise.api.model.ResponseCode;
import com.monitise.helpers.SecurityHelper;
import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.api.model.Response;
import com.monitise.api.model.Role;
import com.monitise.entity.User;
import com.monitise.services.OrganizationService;
import com.monitise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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


    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users", method = RequestMethod.GET)
    public Response<List<User>> getUsers(@PathVariable int organizationId ) throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        checkAuthentication(organizationId);

        List<User> users = userService.getByOrganizationId(organizationId);

        Response response = new Response();
        response.setSuccess(true);
        response.setData(users);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}", method = RequestMethod.GET)
    public Response<User> getSingleUser(@PathVariable int organizationId,
                                        @PathVariable int userId )throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        checkAuthentication(organizationId);

        User user = userService.get(userId);

        Response response = new Response();
        response.setSuccess(true);
        response.setData(user);

        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users", method = RequestMethod.POST)
    public Response<User> addUser(@PathVariable int organizationId,
                                  @RequestBody User employee) throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        checkAuthentication(organizationId);

        Organization organization = organizationService.get(organizationId);
        employee.setOrganization(organization);
        employee.setRole(Role.EMPLOYEE);
        // TODO: change the way this is done
        employee.setUsername(employee.getName()+"."+employee.getSurname());
        employee.setPassword("123");


        validateEmployee(employee);
        // Employee object with id.
        User addedEmployee = userService.addEmployee(employee);

        Response<User> response = new Response<>();
        response.setSuccess(true);
        response.setData(addedEmployee);

        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/organizations/{organizationId}/users/", method = RequestMethod.DELETE)
    public Response<User> deleteUser(@PathVariable int organizationId,
                                     @PathVariable int userId) throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        checkAuthentication(organizationId);

        User soonToBeDeleted = userService.get(userId);
        if( soonToBeDeleted.getOrganization().getId() != organizationId) {
            throw new BaseException(ResponseCode.USER_UNAUTHORIZED_ORGANIZATION,"You are not authorized"
                    + " to perform this action.");
        }

        userService.remove(userId);

        Response<User> response = new Response<>();
        response.setData(soonToBeDeleted);
        response.setSuccess(true);

        return response;
    }

    // region Helper Methods

    private void validateEmployee(User employee) throws BaseException{

        String name = employee.getName();
        String surname = employee.getSurname();
        if( (name == null || name.trim().equals("")) || (surname == null || surname.trim().equals("")) )  {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty user name is not allowed.");
        }

        JobTitle title = employee.getJobTitle();
        Organization organization = employee.getOrganization();
        if( !organizationService.isJobTitleDefined(organization, title) ) {
            throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST, "Job Title you entered is not"
                    + " defined in this company.");
        }

    }

    private void checkAuthentication(int organizationId) throws BaseException {
        // Throws an exception if the user performing this op. is unauthorized.
        User currentUser = securityHelper.getAuthenticatedUser();
        if (currentUser.getRole().equals(Role.MANAGER)) {
            securityHelper.checkUserOrganizationAuthorization(organizationId);
        }
    }

    // endregion

}
