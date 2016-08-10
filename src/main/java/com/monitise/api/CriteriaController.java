package com.monitise.api;

import com.monitise.api.model.CriteriaRequest;
import com.monitise.api.model.CriteriaResponse;
import com.monitise.api.model.CriteriaUserResponse;
import com.monitise.api.model.Role;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.Response;
import com.monitise.api.model.UserResponse;
import com.monitise.entity.Criteria;
import com.monitise.entity.User;
import com.monitise.helpers.SecurityHelper;
import com.monitise.services.CriteriaService;
import com.monitise.services.OrganizationService;
import com.monitise.services.UserService;
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
public class CriteriaController {

    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getAll() {
        List<Criteria> list = criteriaService.getAll();
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(list);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/criteria/{criteriaId}", method = RequestMethod.GET)
    public Response<CriteriaResponse> get(@PathVariable int criteriaId) throws BaseException {
        Criteria criteriaFromService = criteriaService.get(criteriaId);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getAllFilterByOrganizationId(@PathVariable int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(organizationId);
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(list);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.GET)
    public Response<CriteriaResponse> get(@PathVariable int organizationId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteriaFromService = criteriaService.get(criteriaId);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/", method = RequestMethod.POST)
    public Response<CriteriaResponse> add(@PathVariable int organizationId, @RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteria = new Criteria(criteriaRequest.getCriteria(), organizationService.get(organizationId));
        Criteria criteriaFromService = criteriaService.add(criteria);
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteriaFromService.getId(), criteriaFromService.getCriteria());

        Response<CriteriaResponse> response = new Response<>();
        response.setData(criteriaResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.PUT)
    public Response<CriteriaResponse> update(@PathVariable int organizationId, @PathVariable int criteriaId, @RequestBody CriteriaRequest criteriaRequest) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteriaFromService = criteriaService.get(criteriaId);
        criteriaFromService.setCriteria(criteriaRequest.getCriteria());
        criteriaFromService = criteriaService.update(criteriaFromService);

        Response<CriteriaResponse> response = new Response<>();
        response.setData(CriteriaResponse.fromCriteria(criteriaFromService));
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int organizationId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        criteriaService.remove(criteriaId);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getUserCriteriaList(@PathVariable int organizationId, @PathVariable int userId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        User user = userService.get(userId);
        List<Criteria> criteriaList = user.getCriteriaList();
        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(criteriaList);

        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<CriteriaUserResponse> assignCriteriaToUser(@PathVariable int organizationId, @PathVariable int userId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteria = criteriaService.get(criteriaId);
        User userFromService = criteriaService.assignCriteriaToUserById(criteria, userId);
        CriteriaUserResponse criteriaUserResponse = new CriteriaUserResponse(userFromService);

        Response<CriteriaUserResponse> response = new Response<>();
        response.setData(criteriaUserResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/organizations/{organizationId}/users/{userId}/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> removeCriteriaFromUser(@PathVariable int organizationId, @PathVariable int userId, @PathVariable int criteriaId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        Criteria criteria = criteriaService.get(criteriaId);
        criteriaService.removeCriteriaFromUserByID(criteria, userId);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

}

































