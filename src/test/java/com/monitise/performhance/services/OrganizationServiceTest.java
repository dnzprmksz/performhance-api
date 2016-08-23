package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.UpdateOrganizationRequest;
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
public class OrganizationServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobTitleRepository jobTitleRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void get_existingId_shouldGet() throws BaseException {
        Organization fetched = organizationService.get(1);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(1, fetched.getId());
        Assert.assertEquals("Google", fetched.getName());
        Assert.assertEquals(5, fetched.getNumberOfEmployees());
    }

    @Test
    public void get_nonExistingId_shouldNotGet() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST));
        organizationService.get(2016);
    }

    @Test
    public void getAll() {
        List<Organization> organizationList = organizationService.getAll();
        Assert.assertEquals(3, organizationList.size());
        Assert.assertTrue(listContainsOrganization(organizationList, "Google", 5, 1));
        Assert.assertTrue(listContainsOrganization(organizationList, "Monitise", 4, 2));
    }

    @Test
    public void getByName_nonExistingName_shouldNotGet() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.ORGANIZATION_NAME_DOES_NOT_EXIST));
        organizationService.getByName("AksarayBilgisayar");
    }

    @Test
    public void add_existingOrganization_shouldNotAdd() throws BaseException {
        Organization organization = new Organization("Monitise");
        thrown.expect(CustomMatcher.hasCode(ResponseCode.ORGANIZATION_NAME_EXISTS));
        organizationService.add(organization);
    }

    @Test
    public void add_nonExistingOrganization_shouldAdd() throws BaseException {
        Organization organizationFromService = organizationService.add(new Organization("Diyarbakir Star"));
        Assert.assertNotNull(organizationFromService);
        Assert.assertEquals("Diyarbakir Star", organizationFromService.getName());
        Assert.assertEquals(0, organizationFromService.getNumberOfEmployees());
        Assert.assertTrue(organizationFromService.getId() > 0);
    }

    @Test
    public void update_existingOrganization_shouldUpdate() throws BaseException {
        Organization organization = organizationService.get(2);
        organization.setName("Monitise MEA");
        Organization organizationFromService = organizationService.update(organization);

        Assert.assertNotNull(organizationFromService);
        Assert.assertEquals("Monitise MEA", organizationFromService.getName());
    }

    @Test
    public void update_nonExistingOrganization_shouldNotUpdate() throws BaseException {
        Organization organization = new Organization("Apple");
        organization.setId(2864);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST));
        organizationService.update(organization);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void addEmployee_nonExistingUser_shouldAdd() throws BaseException {
        Organization google = organizationService.get(1);
        User googleEmployee = new User("Gilloume", "Pinto", google);
        JobTitle androidDev = jobTitleRepository.findOne(1);
        googleEmployee.setJobTitle(androidDev);

        User addedEmployee = userService.addEmployee(googleEmployee);
        google = organizationService.get(1); // updated google

        Assert.assertNotNull(addedEmployee);
        Assert.assertEquals(1, addedEmployee.getJobTitle().getId());
        Assert.assertEquals("android dev", addedEmployee.getJobTitle().getTitle());
        Assert.assertEquals("Google", google.getName());
        Assert.assertEquals(6, google.getNumberOfEmployees());
        Assert.assertEquals(6, google.getUsers().size());
    }

    @Test
    public void updateFromRequest_validData_shouldUpdate() throws BaseException {
        UpdateOrganizationRequest updateOrganizationRequest = new UpdateOrganizationRequest();
        updateOrganizationRequest.setName("E-Corp");
        Organization organizationFromService = organizationService.updateFromRequest(updateOrganizationRequest, 1);

        Assert.assertNotNull(organizationFromService);
        Assert.assertEquals("E-Corp", organizationFromService.getName());
    }

    @Test
    public void isJobTitleDefined_existingJobTitle_shouldReturnTrue() throws BaseException {
        boolean result = organizationService.isJobTitleDefined(1, 1);
        Assert.assertTrue(result);
    }

    @Test
    public void isJobTitleDefined_nonExistingJobTitle_shouldThrowException() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST));
        organizationService.isJobTitleDefined(1, 17);
    }

    @Test
    public void isJobTitleDefined_differentOrganization_shouldReturnFalse() throws BaseException {
        boolean result = organizationService.isJobTitleDefined(17, 1);
        Assert.assertFalse(result);
    }

    @Test
    public void isTeamIdDefined_existingTeamId_shouldReturnTrue() throws BaseException {
        boolean result = organizationService.isTeamIdDefined(1, 1);
        Assert.assertTrue(result);
    }

    @Test
    public void isTeamIdDefined_nonExistingTeamId_shouldThrowException() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.TEAM_ID_DOES_NOT_EXIST));
        organizationService.isTeamIdDefined(1, 21);
    }

    @Test
    public void isTeamIdDefined_differentOrganization_shouldReturnFalse() throws BaseException {
        boolean result = organizationService.isTeamIdDefined(2, 1);
        Assert.assertFalse(result);
    }

    // region Helper Methods

    private boolean listContainsOrganization(List<Organization> list, String organizationName,
                                             int numberOfEmployees, int id) {
        for (Organization organization : list) {
            if (organization.getName().equals(organizationName)
                    && organization.getId() == id && organization.getNumberOfEmployees() == numberOfEmployees) {
                return true;
            }
        }
        return false;
    }

    private boolean listContainsUser(List<User> list, int id, String name, String surname) {
        for (User user : list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }

    // endregion

}