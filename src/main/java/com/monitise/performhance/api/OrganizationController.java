package com.monitise.performhance.api;

import com.monitise.performhance.api.model.AddOrganizationRequest;
import com.monitise.performhance.api.model.CriteriaResponse;
import com.monitise.performhance.api.model.OrganizationJobTitleResponse;
import com.monitise.performhance.api.model.OrganizationResponse;
import com.monitise.performhance.api.model.OrganizationUserResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.api.model.SimplifiedOrganization;
import com.monitise.performhance.api.model.SimplifiedTeam;
import com.monitise.performhance.api.model.UpdateOrganizationRequest;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
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

import java.util.List;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    // region Dependencies

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private RelationshipHelper relationshipHelper;

    // endregion

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<SimplifiedOrganization>> getAll() {
        List<Organization> list = organizationService.getAll();
        List<SimplifiedOrganization> responseList = SimplifiedOrganization.fromList(list);
        Response<List<SimplifiedOrganization>> response = new Response<>();
        response.setSuccess(true);
        response.setData(responseList);
        return response;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<OrganizationResponse> add(@RequestBody AddOrganizationRequest addOrganizationRequest)
            throws BaseException {
        validateAddOrganizationRequest(addOrganizationRequest);
        Organization organization = new Organization(addOrganizationRequest.getOrganizationName());
        Organization addedOrganization = organizationService.add(organization);

        User manager = new User(
                addOrganizationRequest.getManagerName(),
                addOrganizationRequest.getManagerSurname(),
                addedOrganization,
                Role.MANAGER,
                addOrganizationRequest.getUsername(),
                addOrganizationRequest.getPassword()
        );
        userService.addManager(manager);
        addedOrganization.setManager(manager);
        addedOrganization = organizationService.update(addedOrganization);

        OrganizationResponse responseOrganization = OrganizationResponse.fromOrganization(addedOrganization);
        Response<OrganizationResponse> response = new Response<>();
        response.setData(responseOrganization);
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
    public Response<OrganizationResponse> get(@PathVariable int organizationId) throws BaseException {
        Organization organizationFromService = organizationService.get(organizationId);
        securityHelper.checkAuthentication(organizationId);

        OrganizationResponse responseOrganization = OrganizationResponse.fromOrganization(organizationFromService);
        Response<OrganizationResponse> response = new Response<>();
        response.setData(responseOrganization);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{organizationId}", method = RequestMethod.PUT)
    public Response<OrganizationResponse> update(@RequestBody UpdateOrganizationRequest updateOrganizationRequest,
                                                 @PathVariable int organizationId) throws BaseException {
        securityHelper.checkAuthentication(organizationId);
        organizationService.updateFromRequest(updateOrganizationRequest, organizationId);
        Organization organization = organizationService.get(organizationId);

        OrganizationResponse organizationResponse = OrganizationResponse.fromOrganization(organization);
        Response<OrganizationResponse> response = new Response<>();
        response.setData(organizationResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
    public Response<Object> delete(@PathVariable int organizationId) throws BaseException {
        securityHelper.checkAuthentication(organizationId);
        organizationService.remove(organizationId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void validateAddOrganizationRequest(AddOrganizationRequest request) throws BaseException {
        String name = request.getOrganizationName();
        if (Util.isNullOrEmpty(name)) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty organization name is not allowed.");
        } else if (doesNameExists(name)) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_EXISTS,
                    "Given name is used by another organization.");
        }

        if (Util.isNullOrEmpty(request.getManagerName())) {
            throw new BaseException(ResponseCode.USER_NAME_INVALID, "Empty manager name is not allowed.");
        }

        if (Util.isNullOrEmpty(request.getManagerSurname())) {
            throw new BaseException(ResponseCode.USER_SURNAME_INVALID, "Empty manager surname is not allowed.");
        }

        String username = request.getUsername();
        if (Util.isNullOrEmpty(username)) {
            throw new BaseException(ResponseCode.USERNAME_INVALID, "Empty username is not allowed.");
        }
        userService.ensureUsernameUniqueness(username);

        if (Util.isNullOrEmpty(request.getPassword())) {
            throw new BaseException(ResponseCode.PASSWORD_INVALID, "Empty password is not allowed.");
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