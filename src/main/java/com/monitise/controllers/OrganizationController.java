package com.monitise.controllers;

import com.monitise.models.Organization;
import com.monitise.models.Response;
import com.monitise.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

}