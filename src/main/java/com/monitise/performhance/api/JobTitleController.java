package com.monitise.performhance.api;

import com.monitise.performhance.api.model.AddJobTitleRequest;
import com.monitise.performhance.api.model.ExtendedResponse;
import com.monitise.performhance.api.model.JobTitleResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.UpdateJobTitleRequest;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.helpers.Util;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.JobTitleService;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.UserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/jobTitles")
public class JobTitleController {

    // region Dependencies

    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private RelationshipHelper relationshipHelper;
    @Autowired
    private CriteriaService criteriaService;

    // endregion

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<JobTitleResponse>> getAll() throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        List<JobTitle> list = jobTitleService.getListFilterByOrganizationId(organizationId);

        List<JobTitleResponse> responseList = JobTitleResponse.fromList(list);
        Response<List<JobTitleResponse>> response = new Response<>();
        response.setData(responseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<JobTitleResponse> add(@RequestBody AddJobTitleRequest addJobTitleRequest) throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        validateAddJobTitleRequest(addJobTitleRequest);
        Organization organization = organizationService.get(organizationId);
        JobTitle jobTitle = new JobTitle(addJobTitleRequest.getTitle(), organization);
        JobTitle jobTitleFromService = jobTitleService.add(jobTitle);

        JobTitleResponse jobTitleResponse = new JobTitleResponse(jobTitleFromService);
        Response<JobTitleResponse> response = new Response<>();
        response.setData(jobTitleResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{jobTitleId}", method = RequestMethod.GET)
    public Response<JobTitleResponse> get(@PathVariable int jobTitleId) throws BaseException {
        checkAuthentication(jobTitleId);
        JobTitle jobTitle = jobTitleService.get(jobTitleId);

        JobTitleResponse jobTitleResponse = new JobTitleResponse(jobTitle);
        Response<JobTitleResponse> response = new Response<>();
        response.setData(jobTitleResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{jobTitleId}", method = RequestMethod.PUT)
    public Response<JobTitleResponse> update(@RequestBody UpdateJobTitleRequest updateJobTitleRequest,
                                             @PathVariable int jobTitleId) throws BaseException {
        validateUpdateRequest(updateJobTitleRequest);
        checkAuthentication(jobTitleId);

        JobTitle jobTitle = jobTitleService.get(jobTitleId);
        jobTitle.setTitle(updateJobTitleRequest.getTitle());
        JobTitle jobTitleFromService = jobTitleService.update(jobTitle);

        JobTitleResponse jobTitleResponse = JobTitleResponse.fromJobTitle(jobTitleFromService);
        Response<JobTitleResponse> response = new Response<>();
        response.setData(jobTitleResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{jobTitleId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int jobTitleId) throws BaseException {
        checkAuthentication(jobTitleId);
        jobTitleService.remove(jobTitleId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{jobTitleId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToJobTitleUsers(@PathVariable int jobTitleId,
                                                          @PathVariable int criteriaId) throws BaseException {
        checkAuthentication(jobTitleId);
        securityHelper.checkAuthentication(criteriaService.get(criteriaId).getOrganization().getId());

        ArrayList<Integer> existingUserList = criteriaService.assignCriteriaToJobTitle(criteriaId, jobTitleId);
        ExtendedResponse<Object> response = new ExtendedResponse<>();
        response.setMessage(Util.generateExistingUsersMessage(existingUserList));
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void validateUpdateRequest(UpdateJobTitleRequest updateJobTitleRequest) throws BaseException {
        String title = updateJobTitleRequest.getTitle();
        if (Util.isNullOrEmpty(title)) {
            throw new BaseException(ResponseCode.JOB_TITLE_UPDATE_EMPTY_TITLE, "Title cannot be empty.");
        }
    }

    private void validateAddJobTitleRequest(AddJobTitleRequest request) throws BaseException {
        Util.isNullOrEmpty(request.getTitle());
    }

    private void checkAuthentication(int jobTitleId) throws BaseException {
        JobTitle jobTitle = jobTitleService.get(jobTitleId);
        Organization organization = jobTitle.getOrganization();
        int organizationId = organization.getId();
        securityHelper.checkAuthentication(organizationId);
    }

    // endregion

}