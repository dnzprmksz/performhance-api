package com.monitise.helpers;

import com.monitise.models.BaseException;
import com.monitise.models.ResponseCode;
import com.monitise.models.User;
import com.monitise.services.UserService;
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