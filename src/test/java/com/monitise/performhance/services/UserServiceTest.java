package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.api.model.UpdateUserRequest;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.CustomMatcher;
import com.monitise.performhance.repositories.JobTitleRepository;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
@Transactional
public class UserServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private JobTitleRepository jobTitleRepository;

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void add_nonExistingEmployee_shouldAdd() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        JobTitle iosDev = jobTitleRepository.findOne(3);
        User user = new User("Hans", "Müller", monitise, iosDev, "hans", "123");
        User userFromService = userService.addEmployee(user);

        Assert.assertNotNull(userFromService);
        Assert.assertEquals(user.getName(), userFromService.getName());
        Assert.assertEquals(user.getSurname(), userFromService.getSurname());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void add_employeeExistingNameSurname_shouldAdd() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        JobTitle iosDev = jobTitleRepository.findOne(3);
        User user = new User("Bilge", "Olmez", monitise, iosDev, "bilge", "123");
        User addedUser = userService.addEmployee(user);

        Assert.assertNotNull(addedUser);
        Assert.assertEquals(user.getName(), addedUser.getName());
        Assert.assertEquals(user.getSurname(), addedUser.getSurname());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void add_employeeExistingUsername_shouldNotAdd() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        JobTitle iosDev = jobTitleRepository.findOne(3);
        User user = new User("Ali", "Yılmaz", monitise, iosDev, "monitise.manager", "123");

        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_USERNAME_ALREADY_TAKEN));
        userService.addEmployee(user);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void addEmployee_managerUser_shouldNotAdd() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        User user = new User("Ali", "Yılmaz", monitise, Role.MANAGER, "manager", "123");
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ROLE_INCORRECT));
        userService.addEmployee(user);
    }

    @Test()
    @WithMockUser(roles = {"MANAGER"})
    public void addManager_managerUser_shouldAdd() throws BaseException {
        Organization pozitron = organizationRepository.findOne(3);
        User user = new User("Ali", "Yılmaz", pozitron, Role.MANAGER, "manager", "123");
        User userFromService = userService.addManager(user);

        Assert.assertNotNull(userFromService);
        Assert.assertEquals(user.getName(), userFromService.getName());
        Assert.assertEquals(user.getSurname(), userFromService.getSurname());
        Assert.assertEquals(user.getUsername(), userFromService.getUsername());
        Assert.assertEquals(user.getPassword(), userFromService.getPassword());
        Assert.assertEquals(user.getRole(), userFromService.getRole());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void addManager_employeeUser_shouldNotAdd() throws BaseException {
        Organization pozitron = organizationRepository.findOne(3);
        User user = new User("Ahmet", "Han", pozitron, Role.EMPLOYEE, "ahmet.han", "123");
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ROLE_INCORRECT));
        userService.addManager(user);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void get_existingUserName() throws BaseException {
        User fetchedUser = userService.get(7);

        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals("Bilge", fetchedUser.getName());
        Assert.assertEquals("Olmez", fetchedUser.getSurname());

    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void get_nonExistingUserName() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        userService.get(1884);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void getByUsername_existingUsername_shouldGet() throws BaseException {
        User user = userService.getByUsername("bilge.olmez");
        Assert.assertNotNull(user);
        Assert.assertEquals("Bilge", user.getName());
        Assert.assertEquals("Olmez", user.getSurname());
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void getByUsername_nonExistingUsername_shouldNotGet() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_USERNAME_DOES_NOT_EXIST));
        userService.getByUsername("bilge.sonmez");
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void delete_existingId() throws BaseException {
        userService.remove(7);
        Organization monitise = organizationRepository.findOne(2);

        Assert.assertFalse(listContainsUser(monitise.getUsers(), 7, "Bilge", "Olmez"));
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        userService.get(7);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void delete_nonExistingId() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        userService.remove(1884);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void updateFromRequest_validData_shouldUpdate() throws BaseException {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Arda");
        updateUserRequest.setSurname("Yılmaz");
        updateUserRequest.setPassword("12345");
        updateUserRequest.setJobTitleId(5);
        User userFromService = userService.updateFromRequest(updateUserRequest, 7);

        Assert.assertNotNull(userFromService);
        Assert.assertEquals(updateUserRequest.getName(), userFromService.getName());
        Assert.assertEquals(updateUserRequest.getSurname(), userFromService.getSurname());
        Assert.assertEquals(updateUserRequest.getPassword(), userFromService.getPassword());
        Assert.assertEquals(updateUserRequest.getJobTitleId(), userFromService.getJobTitle().getId());
    }

    private boolean listContainsUser(List<User> list, int id, String name, String surname) {
        for (User user : list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }

}