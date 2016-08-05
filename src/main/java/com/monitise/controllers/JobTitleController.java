package com.monitise.controllers;

import com.monitise.models.BaseException;
import com.monitise.models.JobTitle;
import com.monitise.models.Organization;
import com.monitise.models.Response;
import com.monitise.models.ResponseCode;
import com.monitise.models.User;
import com.monitise.services.JobTitleService;
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

@RequestMapping("/jobTitles")
@RestController
public class JobTitleController {

    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<JobTitle>> getAll() {
        List<JobTitle> list = jobTitleService.getAll();

        Response<List<JobTitle>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Response<JobTitle> get(@PathVariable int id) throws BaseException {
        JobTitle jobTitle = jobTitleService.get(id);

        Response<JobTitle> response = new Response<>();
        response.setData(jobTitle);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organization/{organizationId}", method = RequestMethod.GET)
    public Response<List<JobTitle>> getByOrganization(@PathVariable int organizationId) throws BaseException {
        checkManagerAuthorization(organizationId);
        List<JobTitle> list = jobTitleService.getListFilterByOrganizationId(organizationId);

        Response<List<JobTitle>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organization/{organizationId}", method = RequestMethod.POST)
    public Response<JobTitle> add(@RequestBody JobTitle jobTitle, @PathVariable int organizationId) throws BaseException {
        checkManagerAuthorization(organizationId);
        Organization organization = organizationService.get(organizationId);
        JobTitle jobTitleFromService = jobTitleService.add(jobTitle);
        Organization organizationFromService = organizationService.addJobTitle(jobTitle, organization);

        Response<JobTitle> response = new Response<>();
        response.setData(jobTitleFromService);
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void checkManagerAuthorization(int organizationId) throws BaseException {
        User authenticatedUser = userService.getAuthenticatedUser();
        if (authenticatedUser.getOrganization().getId() != organizationId) {
            throw new BaseException(ResponseCode.USER_UNAUTHORIZED_ORGANIZATION, "You are not authorized to see this organization's job titles.");
        }
    }

    // endregion

}