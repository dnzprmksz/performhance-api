package com.monitise.controllers;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<Organization>> getAll() {
        List<Organization> list = organizationService.getAll();

        Response<List<Organization>> response = new Response<>();
        response.setSuccess(true);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<Organization> get(@PathVariable int id) throws BaseException {
        Organization organization = organizationService.get(id);

        Response<Organization> response = new Response<>();
        response.setSuccess(true);
        response.setData(organization);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{id}/users", method = RequestMethod.POST)
    public User addUser(@PathVariable int id, @RequestBody User employee) throws BaseException {
        //throws an exception if the user performing this op. is unauthorized
        User currentUser = userService.getAuthenticatedUser();
        if ( currentUser.getRole().equals(Role.MANAGER) ) {
            userService.checkUserOrganizationAuthorization(id);
        }

        Organization organization = organizationService.get(id);
        employee.setOrganization(organization);
        employee.setRole(Role.EMPLOYEE);
        // TODO: change the way this is done
        employee.setUsername(employee.getName()+"."+employee.getSurname());
        employee.setPassword("123");


        validateEmployee(employee);

        User addedEmployee = userService.addEmployee(employee);
        return addedEmployee;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<Organization> add(@RequestBody Organization organization) throws BaseException {
        System.out.println("add Org");
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