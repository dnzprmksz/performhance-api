package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.JobTitleRepository;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
@Transactional
public class UserServiceTest {

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

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void add_employeeExistingUsername_shouldNotAdd() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        JobTitle iosDev = jobTitleRepository.findOne(3);
        User user = new User("Ali", "Yılmaz", monitise, iosDev, "monitise.manager", "123");
        User addedUser = userService.addEmployee(user);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void get_existingUserName() throws BaseException {
        User fetchedUser = userService.get(7);

        Assert.assertNotNull(fetchedUser);
        Assert.assertEquals("Bilge", fetchedUser.getName());
        Assert.assertEquals("Olmez", fetchedUser.getSurname());

    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void get_nonExistingUserName() throws BaseException {
        userService.get(1884);
    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void delete_existingId() throws BaseException {
        Organization monitise = organizationRepository.findOne(2);
        userService.remove(7);
        userService.get(7);
    }

    @Test(expected = BaseException.class)
    @WithMockUser(roles = {"MANAGER"})
    public void delete_nonExistingId() throws BaseException {
        userService.remove(1884);
    }

}