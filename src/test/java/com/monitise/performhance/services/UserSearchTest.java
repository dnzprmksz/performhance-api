package com.monitise.performhance.services;


import com.monitise.performhance.AppConfig;
import com.monitise.performhance.BaseException;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserSearchTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithTeamId() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, "1", undef, undef, undef);
        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsBundle(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsBundle(foundUsers, 3, "Faruk", "Gulmez"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithPartialName() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, "Pel", undef);
        Assert.assertEquals(1, foundUsers.size());
        Assert.assertTrue(listContainsBundle(foundUsers, 2, "Pelin", "Sonmez"));
    }

    @Test
    @WithMockUser(roles ={"MANAGER"})
    public void searchWithPartialSurname() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, undef, "mez");
        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsBundle(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsBundle(foundUsers, 3, "Faruk", "Gulmez"));
    }

    private void genUserNamePassword(User u) {
        String name = u.getName();
        String surname = u.getSurname();
        u.setUsername(name + "." + surname);
        u.setPassword(surname);
    }

    private void setUserNamePassword(User u, String userName, String password) {
        u.setUsername(userName);
        u.setPassword(password);
    }

    private boolean listContainsBundle(List<User> list, int id, String name, String surname) {
        for (User user: list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }


}