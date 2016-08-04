package com.monitise.controllers;

import com.monitise.models.*;
import com.monitise.services.EmployeeService;
import com.monitise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<Iterable<Organization>> getAll() {

        // Initialize response object.
        Response<Iterable<Organization>> response = new Response<>();

        // Get all organizations from service.
        Iterable<Organization> list = organizationService.getAll();

        // Set response details.
        response.setSuccess(true);
        response.setData(list);

        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<Organization> get(@PathVariable int id) throws BaseException {

        // Initialize response object.
        Response<Organization> response = new Response<>();

        // Get the organization with given ID from service.
        Organization organization = organizationService.get(id);

        // Set response details.
        response.setSuccess(true);
        response.setData(organization);

        return response;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<Organization> add(@RequestBody Organization organization) throws BaseException {

        // Validate name.
        validateName(organization.getName());

        // Initialize response object.
        Response<Organization> response = new Response<>();

        // Add the given organization.
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

        // Check for empty name.
        if (name == null || name.trim().equals("")) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty organization name is not allowed.");
        }
        // Check for existing name.
        else if (doesNameExists(name)) {
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