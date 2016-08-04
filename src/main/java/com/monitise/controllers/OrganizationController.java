package com.monitise.controllers;

import com.monitise.models.*;
import com.monitise.services.EmployeeService;
import com.monitise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<Organization>> getAll() {

        Response<List<Organization>> response = new Response<>();

        List<Organization> list = organizationService.getAll();

        // Set response details.
        response.setSuccess(true);
        response.setData(list);

        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<Organization> get(@PathVariable int id) throws BaseException {

        Response<Organization> response = new Response<>();

        Organization organization = organizationService.get(id);

        // Set response details.
        response.setSuccess(true);
        response.setData(organization);

        return response;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<Organization> add(@RequestBody Organization organization) throws BaseException {

        validateName(organization.getName());
        Response<Organization> response = new Response<>();

        Organization organizationFromService = organizationService.add(organization);

        // Create management user for the organization.
        Manager manager = new Manager(organizationFromService);
        employeeService.add(manager);
        organizationFromService.setManager(manager);

        // Update organization with manager ID.
        organizationFromService = organizationService.update(organizationFromService);

        // Set response details.
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

    // endregion

}