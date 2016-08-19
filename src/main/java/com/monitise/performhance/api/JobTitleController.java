package com.monitise.performhance.api;

import com.monitise.performhance.api.model.AddJobTitleRequest;
import com.monitise.performhance.api.model.ExtendedResponse;
import com.monitise.performhance.api.model.JobTitleResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.UpdateJobTitleRequest;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
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
    public Response<JobTitleResponse> addJobTitle(@RequestBody AddJobTitleRequest addJobTitleRequest)
            throws BaseException {
        User manager = securityHelper.getAuthenticatedUser();
        int organizationId = manager.getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);

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
        JobTitle jobTitle = jobTitleService.get(jobTitleId);
        securityHelper.checkAuthentication(jobTitle.getOrganization().getId());
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
        JobTitle jobTitle = jobTitleService.get(jobTitleId);
        securityHelper.checkAuthentication(jobTitle.getOrganization().getId());
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
        securityHelper.checkAuthentication(jobTitleService.get(jobTitleId).getOrganization().getId());
        jobTitleService.remove(jobTitleId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{jobTitleId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToJobTitleUsers(@PathVariable int jobTitleId,
                                                          @PathVariable int criteriaId) throws BaseException {
        int organizationId = jobTitleService.get(jobTitleId).getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        securityHelper.checkAuthentication(criteriaId);

        ArrayList<Integer> existingUserList = criteriaService.assignCriteriaToJobTitle(criteriaId, jobTitleId);
        ExtendedResponse<Object> response = new ExtendedResponse<>();
        response.setMessage(generateExistingUsersMessage(existingUserList));
        response.setSuccess(true);
        return response;
    }

    private String generateExistingUsersMessage(ArrayList<Integer> existingUserList) {
        String message = null;
        if (!existingUserList.isEmpty()) {
            message = "Completed successfully, however, the criteria was already assigned for following users:";
            for (int userId : existingUserList) {
                message += " " + userId;
            }
        }
        return message;
    }

}