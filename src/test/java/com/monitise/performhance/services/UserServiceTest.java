package com.monitise.performhance.services;

<<<<<<< HEAD
import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.BaseException;
=======
import com.monitise.AppConfig;
import com.monitise.performhance.BaseException;
>>>>>>> 80662b83980dcff59969104c04174713cc408bfd
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserServiceTest {

    private static boolean init = false;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Before
    public void setup() throws BaseException {
        organizationService.add(new Organization("Monitise"));
        organizationService.add(new Organization("Palantir"));
    }

    @After
    public void cleanup() {
        userRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void add_EmployeeWithoutAuthentication() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        userService.addEmployee(new User("Employee", "Shouldn't add this", organization));
    }

    @Test(expected = AuthenticationCredentialsNotFoundException.class)
    public void add_ManagerWithoutAuthentication() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        userService.addManager(new User("Employee", "Shouldn't add this", organization));
    }

    @Test(expected = AccessDeniedException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void add_WithoutProperAuthorization() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        userService.addManager(new User("Manager", "Shouldn't add this", organization));
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void add_NonExistingEmployee_shouldAdd() throws BaseException {

        Organization organization = organizationService.getByName("Monitise");
        User user = new User("Deniz", "Parmaksiz", organization);
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
        final String username = "G-ZUS";
        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        user.setUsername(username);

        User addedUser = userService.addEmployee(user);
        User getUser = userService.getByUsername(username);

        Assert.assertNotNull(addedUser);
        Assert.assertEquals(user.getName(), addedUser.getName());
        Assert.assertEquals(user.getSurname(), addedUser.getSurname());

        Assert.assertNotNull(getUser);
        Assert.assertEquals(getUser.getName(), addedUser.getName());
        Assert.assertEquals(getUser.getSurname(), addedUser.getSurname());

    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void get_nonExistingUserName() throws BaseException {
        final String username = "G-ZUS";
        final String nonExistenceUsername = "JESUS";

        Organization organization = organizationService.getByName("Monitise");

        User user = new User("Mustafa", "Peksen", organization);
        user.setUsername(username);

        User addedUser = userService.addEmployee(user);
        User getUser = userService.getByUsername(nonExistenceUsername);

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

}
