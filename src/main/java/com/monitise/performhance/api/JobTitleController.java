package com.monitise.performhance.api;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.services.JobTitleService;
import com.monitise.performhance.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JobTitleController {

    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private SecurityHelper securityHelper;

    @RequestMapping(value = "/jobTitles/", method = RequestMethod.GET)
    public Response<List<JobTitle>> getAll() {
        List<JobTitle> list = jobTitleService.getAll();

        Response<List<JobTitle>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/jobTitles/{organizationId}", method = RequestMethod.GET)
    public Response<JobTitle> get(@PathVariable int id) throws BaseException {
        JobTitle jobTitle = jobTitleService.get(id);

        Response<JobTitle> response = new Response<>();
        response.setData(jobTitle);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/jobTitles/", method = RequestMethod.GET)
    public Response<List<JobTitle>> getJobTitleByOrganization(@PathVariable int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<JobTitle> list = jobTitleService.getListFilterByOrganizationId(organizationId);

        Response<List<JobTitle>> response = new Response<>();
        response.setData(list);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/jobTitles/{jobTitleId}", method = RequestMethod.GET)
    public Response<JobTitle> getJobTitle(@PathVariable int organizationId,
                                          @PathVariable int jobTitleId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        JobTitle jobTitle = jobTitleService.get(jobTitleId);

        Response<JobTitle> response = new Response<>();
        response.setData(jobTitle);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/jobTitles/", method = RequestMethod.POST)
    public Response<JobTitle> addJobTitle(@RequestBody JobTitle jobTitle,
                                          @PathVariable int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        JobTitle jobTitleFromService = jobTitleService.add(jobTitle);

        Response<JobTitle> response = new Response<>();
        response.setData(jobTitleFromService);
        response.setSuccess(true);
        return response;
    }

}