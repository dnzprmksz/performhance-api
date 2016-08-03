package com.monitise.controllers;

import com.monitise.models.BaseException;
import com.monitise.models.Organization;
import com.monitise.models.Response;
import com.monitise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

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

        // Initialize response object.
        Response<Organization> response = new Response<>();

        // Add the given organization.
        Organization organizationFromService = organizationService.add(organization);

        // Set response details.
        response.setSuccess(true);
        response.setData(organizationFromService);

        return response;
    }

}