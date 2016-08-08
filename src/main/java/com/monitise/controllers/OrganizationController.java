package com.monitise.controllers;

import com.monitise.helpers.SecurityHelper;
import com.monitise.models.BaseException;
import com.monitise.models.JobTitle;
import com.monitise.models.Organization;
import com.monitise.models.Response;
import com.monitise.models.ResponseCode;
import com.monitise.models.Role;
import com.monitise.models.User;
import com.monitise.services.JobTitleService;
import com.monitise.services.UserService;
import com.monitise.services.OrganizationService;
import com.monitise.services.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private SecurityHelper securityHelper;
    private static final Logger LOGGER = LogManager.getLogger(OrganizationController.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<Organization>> getAll() {
        String log = "GET /organizations";
        LOGGER.info(log);
        List<Organization> list = organizationService.getAll();

        Response<List<Organization>> response = new Response<>();
        response.setSuccess(true);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<Organization> get(@PathVariable int id) throws BaseException {
        String log = "GET /organizations/" + id;
        LOGGER.info(log);
        Organization organization = organizationService.get(id);

        Response<Organization> response = new Response<>();
        response.setSuccess(true);
        response.setData(organization);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{id}/users", method = RequestMethod.GET)
    public Response< List<User> > getUsers(@PathVariable int id ) throws BaseException {
        //throws an exception if the user performing this op. is unauthorized
        User currentUser = securityHelper.getAuthenticatedUser();
        if ( currentUser.getRole().equals(Role.MANAGER) ) {
            securityHelper.checkUserOrganizationAuthorization(id);
        }

        Response response = new Response();
        response.setSuccess(true);
        List<User> users = userService.getByOrganizationId(id);
        response.setData(users);
        //User addedEmployee = userService.addEmployee(employee);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{org_id}/users/{usr_id}", method = RequestMethod.GET)
    public Response<User> getSingleUser(@PathVariable int org_id, @PathVariable int usr_id )throws BaseException {
        //throws an exception if the user performing this op. is unauthorized
        User currentUser = securityHelper.getAuthenticatedUser();
        if ( currentUser.getRole().equals(Role.MANAGER) ) {
            securityHelper.checkUserOrganizationAuthorization(org_id);
        }

        User user = userService.get(usr_id);

        Response response = new Response();
        response.setSuccess(true);
        response.setData(user);
        //User addedEmployee = userService.addEmployee(employee);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{id}/users", method = RequestMethod.POST)
    public Response<User> addUser(@PathVariable int id, @RequestBody User employee) throws BaseException {
        //throws an exception if the user performing this op. is unauthorized
        User currentUser = securityHelper.getAuthenticatedUser();
        if ( currentUser.getRole().equals(Role.MANAGER) ) {
            securityHelper.checkUserOrganizationAuthorization(id);
        }

        Organization organization = organizationService.get(id);
        employee.setOrganization(organization);
        employee.setRole(Role.EMPLOYEE);
        // TODO: change the way this is done
        employee.setUsername(employee.getName()+"."+employee.getSurname());
        employee.setPassword("123");


        //validateEmployee(employee);
        // employee object with id
        User addedEmployee = userService.addEmployee(employee);

        Response<User> response = new Response<>();
        response.setSuccess(true);
        response.setData(addedEmployee);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{org_id}/users/", method = RequestMethod.DELETE)
    public Response<User> deleteUser(@PathVariable int org_id,
                                     @RequestBody int usr_id) throws BaseException {
        //throws an exception if the user performing this op. is unauthorized
        User currentUser = securityHelper.getAuthenticatedUser();
        if ( currentUser.getRole().equals(Role.MANAGER) ) {
            securityHelper.checkUserOrganizationAuthorization(org_id);
        }

        User soonToBeDeleted = userService.get(usr_id);
        if( soonToBeDeleted.getOrganization().getId() != org_id) {
            throw new BaseException(ResponseCode.USER_UNAUTHORIZED_ORGANIZATION,"You are not authorized"
            + " to perform this action.");
        }

        userService.remove(usr_id);

        Response<User> response = new Response<>();
        response.setData(soonToBeDeleted);
        response.setSuccess(true);
        //User addedEmployee = userService.addEmployee(employee);
        return response;
    }



    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<Organization> add(@RequestBody Organization organization) throws BaseException {
        String  log = "POST /organizations - Organization Name:" + organization.getName();
        LOGGER.info(log);

        validateName(organization.getName());
        Organization organizationFromService = organizationService.add(organization);

        // Create management user for the organization.
        User manager = new User(organizationFromService.getName(), "Manager", organizationFromService, Role.MANAGER);
        // TODO: Change the way how manager account is created. It is fixed for now for test purposes.
        manager.setUsername(organizationFromService.getName().toLowerCase());
        manager.setPassword("admin");
        userService.addManager(manager);
        organizationFromService.setManager(manager);

        // Update organization with manager ID.
        organizationFromService = organizationService.update(organizationFromService);

        Response<Organization> response = new Response<>();
        response.setSuccess(true);
        response.setData(organizationFromService);
        return response;
    }

    // region Helper Methods

    private void validateName(String name) throws BaseException {

        if (name == null || name.trim().equals("")) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty organization name is not allowed.");
        } else if (doesNameExists(name)) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_EXISTS, "Given name is used by another organization.");
        }
    }

    private boolean doesNameExists(String name) {
        try {
            organizationService.getByName(name);
        } catch (BaseException exception) {
            if (exception.getCode() == ResponseCode.ORGANIZATION_NAME_DOES_NOT_EXIST) {
                return false;
            }
        }

        return true;
    }

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
    // endregion

}