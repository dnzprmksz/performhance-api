package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
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
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SqlGroup({
    @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
    @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private UserService userService;
    @Autowired
    private JobTitleService jobTitleService;

    @Test
    public void getByExistingId() throws BaseException {
        Organization fetched;

        fetched = organizationService.get(1);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(1, fetched.getId());
        Assert.assertEquals("Google", fetched.getName());
        Assert.assertEquals(5, fetched.getNumberOfEmployees());

        fetched = organizationService.get(2);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(2, fetched.getId());
        Assert.assertEquals("Monitise", fetched.getName());
        Assert.assertEquals(3, fetched.getNumberOfEmployees());

    }

    @Test
    public void getAll() {
        List<Organization> organizationList = organizationService.getAll();

        Assert.assertEquals(2, organizationList.size());

        Assert.assertTrue(listContainsOrganization(organizationList, "Google", 5, 1));
        Assert.assertTrue(listContainsOrganization(organizationList, "Monitise", 3, 2));

    }

    @Test(expected = BaseException.class)
    public void getByNonExistingId() throws BaseException {
        Organization fetched = organizationService.get(1000001);
    }

    @Test(expected = BaseException.class)
    public void getByUsername_nonExistingName() throws BaseException {
        Organization organization = organizationService.getByName("AksarayBilgisayar");
    }

    @Test(expected = BaseException.class)
    public void add_existingOrganization_shouldNotAdd() throws BaseException {
        Organization organization = new Organization("Monitise");
        Organization organizationFromRepo = organizationService.add(organization);

    }

    @Test
    public void add_organization_shouldAdd() throws BaseException {
        String organizationName = "Diyarbakir Star";
        Organization addedOrganization = organizationService.add(new Organization(organizationName));

        Assert.assertNotNull(addedOrganization);
        Assert.assertEquals(organizationName, addedOrganization.getName());
        Assert.assertEquals(0, addedOrganization.getNumberOfEmployees());
    }

    @Test
    public void update_existingOrganization() throws BaseException {
        final String currentName = "Kayseir Hali Kilim Turizm";
        final String newName = "Donitise";
        final int newNumberOfEmployees = 17;
        final int expectedId = 3;
        organizationService.add(new Organization(currentName));

        Organization kayseriHaliKilim = organizationService.get(expectedId);
        kayseriHaliKilim.setNumberOfEmployees(newNumberOfEmployees);
        kayseriHaliKilim.setName(newName);
        organizationService.update(kayseriHaliKilim);

        Organization updated = organizationService.get(expectedId);
        Assert.assertNotNull(updated);
        Assert.assertEquals(expectedId, updated.getId());
        Assert.assertEquals(newNumberOfEmployees, updated.getNumberOfEmployees());
        Assert.assertEquals(newName, updated.getName());

    }

    @Test(expected = BaseException.class)
    public void update_non_existingOrganization() throws BaseException {
        Organization nonExistent = new Organization("Oh non non non");
        nonExistent.setId(-7);
        organizationService.update(nonExistent);
    }

    @Test
    @WithMockUser(roles = {"MANAGER"})
    public void addEmployee() throws BaseException {
        Organization google = organizationService.get(1);
        User googleEmployee = new User("Gilloume", "Pinto",google);
        JobTitle androidDev = jobTitleService.get(1);
        googleEmployee.setJobTitle(androidDev);
        User addedEmployee = userService.addEmployee(googleEmployee);
        organizationService.addEmployee(1, 9);
        google = organizationService.get(1); // updated google

        Assert.assertNotNull(addedEmployee);
        Assert.assertEquals(9, addedEmployee.getId());
        Assert.assertEquals(1, addedEmployee.getJobTitle().getId());
        Assert.assertEquals("android dev", addedEmployee.getJobTitle().getTitle());
        Assert.assertEquals("Google", google.getName());
        Assert.assertEquals(6, google.getNumberOfEmployees());
        Assert.assertEquals(6, google.getUsers().size());
        Assert.assertTrue(listContainsUser(google.getUsers(),9,"Gilloume","Pinto"));
    }



    private boolean listContainsOrganization(List<Organization> list, String organizationName,
                                             int numberOfEmployees, int id) {
        for(Organization organization: list) {
            if( organization.getName().equals(organizationName)
                    && organization.getId() == id && organization.getNumberOfEmployees() == numberOfEmployees) {
                return true;
            }
        }
        return false;
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