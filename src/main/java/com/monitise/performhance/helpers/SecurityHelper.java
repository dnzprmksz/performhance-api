package com.monitise.performhance.helpers;

import com.monitise.performhance.api.model.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityHelper {

    @Autowired
    private UserService userService;

    public User getAuthenticatedUser() throws BaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = auth.getName();
        User authenticatedUser = userService.getByUsername(authenticatedUsername);
        return authenticatedUser;
    }

    public void checkUserOrganizationAuthorization(int organizationId) throws BaseException {
        User authenticatedUser = getAuthenticatedUser();
        if (authenticatedUser.getOrganization().getId() != organizationId) {
            throw new BaseException(ResponseCode.USER_UNAUTHORIZED_ORGANIZATION, "You are not authorized for this organization.");
        }
    }

}