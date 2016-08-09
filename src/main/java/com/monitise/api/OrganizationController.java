package com.monitise.api;

import com.monitise.api.model.BaseException;
import com.monitise.entity.Organization;
import com.monitise.api.model.Response;
import com.monitise.api.model.ResponseCode;
import com.monitise.api.model.Role;
import com.monitise.entity.User;
import com.monitise.services.UserService;
import com.monitise.services.OrganizationService;
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

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;


    // TODO: Only available to admin
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<Organization>> getAll() {
        List<Organization> list = organizationService.getAll();

        Response<List<Organization>> response = new Response<>();
        response.setSuccess(true);
        response.setData(list);
        return response;
    }

    @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
    public Response<Organization> get(@PathVariable int id) throws BaseException {
        Organization organization = organizationService.get(id);

        Response<Organization> response = new Response<>();
        response.setSuccess(true);
        response.setData(organization);
        return response;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<Organization> add(@RequestBody Organization organization) throws BaseException {
        validateName(organization.getName());
        Organization organizationFromService = organizationService.add(organization);

        // Create management user for the organization.
        User manager = new User(organizationFromService.getName(), "Manager", organizationFromService, Role.MANAGER);
        // TODO: Change the way how manager account is created. It is fixed for now for test purposes.
        manager.setUsername(organizationFromService.getName().toLowerCase());
        manager.setPassword("admin");
        userService.addManager(manager);
        organizationFromService.setManager(manager);

        // Update organization with manager ID.
        organizationFromService = organizationService.update(organizationFromService);

        Response<Organization> response = new Response<>();
        response.setSuccess(true);
        response.setData(organizationFromService);
        return response;
    }

    // region Helper Methods

    private void validateName(String name) throws BaseException {

        if (name == null || name.trim().equals("")) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID, "Empty organization name is not allowed.");
        } else if (doesNameExists(name)) {
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