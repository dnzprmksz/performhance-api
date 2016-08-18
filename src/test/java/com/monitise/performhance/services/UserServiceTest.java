package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void reallyjusttesting() throws BaseException {
        Organization organization = organizationService.get(1);
        Assert.assertEquals(organization.getName(), "Google");
        Assert.assertEquals(organization.getNumberOfEmployees(), 0);
    }


    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void add_NonExistingEmployee_shouldAdd() throws BaseException {

        Organization organization = organizationService.getByName("Monitise");
        User user = new User("Yamac", "Egemen", organization);
        genUserNamePassword(user);

        User userFromService = userService.addEmployee(user);

        Assert.assertNotNull(userFromService);
        Assert.assertEquals(user.getName(), userFromService.getName());
        Assert.assertEquals(user.getSurname(), userFromService.getSurname());
    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void add_employeeExistingNameSurname_shouldAdd() throws BaseException {

        Organization organization = organizationService.getByName("Monitise");
        User user = new User("Deniz", "Parmaksiz", organization);
        User duplicate = new User("Deniz", "Parmaksiz", organization);
        User addedUser = userService.addEmployee(user);
        User addedDuplicate = userService.addEmployee(duplicate);
    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void add_employeeExistingUsername_shouldNotAdd() throws BaseException {
        final String username = "3mr3";
        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Emre", "Pozer", organization);
        user.setUsername(username);

        User duplicate = new User("Emre", "Sonmez", organization);
        duplicate.setUsername(username);
        User addedUser = userService.addEmployee(user);
        User addedDuplicate = userService.addEmployee(duplicate);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void get_existingUserName() throws BaseException {
        final String username = "FooFighter";
        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        user.setUsername(username);

        User addedUser = userService.addEmployee(user);
        User fetchedUser = userService.getByUsername(username);

        Assert.assertNotNull(addedUser);
        Assert.assertEquals(user.getName(), addedUser.getName());
        Assert.assertEquals(user.getSurname(), addedUser.getSurname());

        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals(fetchedUser.getName(), addedUser.getName());
        Assert.assertEquals(fetchedUser.getSurname(), addedUser.getSurname());

    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void get_nonExistingUserName() throws BaseException {
        final String username = "G-ZUS";
        final String nonExistentUsername = "JESUS";

        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        user.setUsername(username);

        User addedUser = userService.addEmployee(user);
        User getUser = userService.getByUsername(nonExistentUsername);

        Assert.assertNotNull(addedUser);
        Assert.assertEquals(user.getName(), addedUser.getName());
        Assert.assertEquals(user.getSurname(), addedUser.getSurname());

        Assert.assertNull(getUser);

    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void delete_ExistingId() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        User addedUser = userService.addEmployee(user);
        userService.remove(addedUser.getId());

        User fromRepo = userService.get(addedUser.getId());

    }


    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void delete_NonExistingId() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        User addedUser = userService.addEmployee(user);
        userService.remove(34663);

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


}