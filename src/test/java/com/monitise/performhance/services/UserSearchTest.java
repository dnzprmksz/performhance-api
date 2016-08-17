package com.monitise.performhance.services;


import com.monitise.performhance.AppConfig;
import com.monitise.performhance.BaseException;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
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

        Assert.assertEquals(3, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 4, "Pelya", "Petroffski"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithJobTitleId() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, "1", undef, undef);

        Assert.assertEquals(3, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 1, "Google", "Manager"));
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 5, "Fatih", "Songul"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithPartialName() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, "pel", undef);

        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 4, "Pelya", "Petroffski"));
    }

    @Test
    @WithMockUser(roles ={"MANAGER"})
    public void searchWithPartialSurname() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, undef, "mez");

        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 3, "Faruk", "Gulmez"));
    }

    @Test
    @WithMockUser(roles ={"MANAGER"})
    public void searchWithPartialNameAndSurname() throws BaseException {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, "fA", "gul");

        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 5, "Fatih", "Songul"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithPartialNameAndTeamId() {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, "1", undef, "pel", undef);

        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 4, "Pelya", "Petroffski"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithJobTitleIdAndTeamId() {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, "1", "2", undef, undef);

        Assert.assertEquals(2, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 4, "Pelya", "Petroffski"));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithNonExistingTeamId() {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, "10", undef, undef, undef);

        Assert.assertEquals(0, foundUsers.size());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithNonExistingPartialNames() {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, "AASDFGHJ", undef);
        Assert.assertEquals(0, foundUsers.size());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void searchWithoutAnyInput() {
        String undef = UserService.UNDEFINED;
        List<User> foundUsers = userService.searchUsers(1, undef, undef, undef, undef);

        Assert.assertEquals(5, foundUsers.size());
        Assert.assertTrue(listContainsUser(foundUsers, 1, "Google", "Manager"));
        Assert.assertTrue(listContainsUser(foundUsers, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(foundUsers, 4, "Pelya", "Petroffski"));
        Assert.assertTrue(listContainsUser(foundUsers, 5, "Fatih", "Songul"));
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

    private boolean listContainsUser(List<User> list, int id, String name, String surname) {
        for (User user: list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }


}