package com.monitise.performhance.api;

import com.monitise.performhance.api.model.CriteriaRequest;
import com.monitise.performhance.api.model.CriteriaResponse;
import com.monitise.performhance.api.model.ExtendedResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.helpers.Util;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.JobTitleService;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.TeamService;
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

import static com.monitise.performhance.helpers.Util.generateExistingUsersMessage;

@RestController
@RequestMapping("/criteria")
public class CriteriaController {

    // region Dependencies

    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private RelationshipHelper relationshipHelper;
    @Autowired
    private JobTitleService jobTitleService;

    // endregion

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getAll() throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(organizationId);

        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(list);
        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<CriteriaResponse> add(@RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        Criteria criteria = new Criteria(criteriaRequest.getCriteria(), organizationService.get(organizationId));
        Criteria criteriaFromService = criteriaService.add(criteria);

        CriteriaResponse criteriaResponse = CriteriaResponse.fromCriteria(criteriaFromService);
        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{criteriaId}", method = RequestMethod.GET)
    public Response<CriteriaResponse> get(@PathVariable int criteriaId) throws BaseException {
        checkAuthentication(criteriaId);
        Criteria criteriaFromService = criteriaService.get(criteriaId);

        CriteriaResponse criteriaResponse = CriteriaResponse.fromCriteria(criteriaFromService);
        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{criteriaId}", method = RequestMethod.PUT)
    public Response<CriteriaResponse> update(@PathVariable int criteriaId,
                                             @RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        checkAuthentication(criteriaId);
        Criteria criteriaFromService = criteriaService.updateFromRequest(criteriaRequest, criteriaId);

        CriteriaResponse criteriaResponse = CriteriaResponse.fromCriteria(criteriaFromService);
        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int criteriaId) throws BaseException {
        checkAuthentication(criteriaId);
        criteriaService.remove(criteriaId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{criteriaId}", method = RequestMethod.POST)
    public Response<Object> assignCriteriaToGroup(@PathVariable int criteriaId,
                                                  @RequestBody List<Integer> userIdList) throws BaseException {
        checkAuthentication(criteriaId);
        ArrayList<Integer> existingUserList = criteriaService.assignCriteriaToUserList(criteriaId, userIdList);
        ExtendedResponse<Object> response = new ExtendedResponse<>();
        response.setMessage(generateExistingUsersMessage(existingUserList));
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void checkAuthentication(int criteriaId) throws BaseException {
        Criteria criteria = criteriaService.get(criteriaId);
        Organization organization = criteria.getOrganization();
        int organizationId = organization.getId();
        securityHelper.checkAuthentication(organizationId);
    }

    // endregion

}