package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.api.model.BaseException;
import com.monitise.entity.Organization;
import com.monitise.repositories.OrganizationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class OrganizationServiceTest {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Before
    public void setup() throws BaseException {
        organizationService.add(new Organization("Google"));
        organizationService.add(new Organization("Monitise"));
        organizationService.add(new Organization("Palantir"));
    }

//    @After
//    public void cleanup() {
//        organizationRepository.deleteAll();
//    }

    @Test
    public void getAll() {
        List<Organization> organizationList = organizationService.getAll();

        Assert.assertEquals(3, organizationList.size());

        Assert.assertEquals(1, organizationList.get(0).getId() );
        Assert.assertEquals(2, organizationList.get(1).getId() );
        Assert.assertEquals(3, organizationList.get(2).getId() );

        Assert.assertEquals("Google", organizationList.get(0).getName());
        Assert.assertEquals("Monitise", organizationList.get(1).getName());
        Assert.assertEquals("Palantir", organizationList.get(2).getName());

    }

    @Test
    public void getByExistingId() throws BaseException {
        Organization fetched;

        fetched = organizationService.get(1);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(1, fetched.getId());
        Assert.assertEquals("Google", fetched.getName());
        Assert.assertEquals(0, fetched.getNumberOfEmployees());

        fetched = organizationService.get(2);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(2, fetched.getId());
        Assert.assertEquals("Monitise", fetched.getName());
        Assert.assertEquals(0, fetched.getNumberOfEmployees());

        fetched = organizationService.get(3);
        Assert.assertNotNull(fetched);
        Assert.assertEquals(3, fetched.getId());
        Assert.assertEquals("Palantir", fetched.getName());
        Assert.assertEquals(0, fetched.getNumberOfEmployees());

    }

    @Test(expected = BaseException.class)
    public void getByNonExistingId() throws BaseException {
        Organization fetched = organizationService.get(1000001);
    }

    @Test
    public void getByUsername_existingName() throws BaseException {

        Organization organizationFromService = organizationService.getByName("Pozitron");
        Assert.assertNotNull(organizationFromService);
        Assert.assertEquals("Pozitron", organizationFromService.getName());
    }

    @Test(expected = BaseException.class)
    public void getByUsername_nonExistingName() throws BaseException{
        Organization organization = organizationService.getByName("AksarayBilgisayar");
    }

    @Test(expected = BaseException.class)
    public void add_existingOrganization_shouldNotAdd() throws BaseException {
        Organization organization = new Organization("Monitise");
        Organization organizationFromRepo = null;
        organizationFromRepo = organizationService.add(organization);

        Assert.assertNotNull(organizationFromRepo);
        Assert.assertEquals(organization.getName(), organizationFromRepo.getName());
    }

    @Test
    public void add_organization_shouldAdd() throws BaseException {
        String organizationName = "Diyarbakir Star";
        Organization addedOrganization = organizationService.add(new Organization(organizationName));

        Assert.assertNotNull(addedOrganization);
        Assert.assertEquals(4, addedOrganization.getId());
        Assert.assertEquals(organizationName, addedOrganization.getName());
        Assert.assertEquals(0, addedOrganization.getNumberOfEmployees());
    }

    @Test
    public void update_existingOrganization() throws BaseException {
        final String newName = "Donitise";
        final int newNumberOfEmployees = 17;
        final int ID = 2;

        Organization monitise = organizationService.get(ID);
        monitise.setNumberOfEmployees(newNumberOfEmployees);
        monitise.setName(newName);
        organizationService.update(monitise);

        Organization updated = organizationService.get(ID);
        Assert.assertNotNull(updated);
        Assert.assertEquals(ID, updated.getId());
        Assert.assertEquals(newNumberOfEmployees, updated.getNumberOfEmployees());
        Assert.assertEquals(newName, updated.getName());

    }

    @Test(expected = BaseException.class)
    public void update_non_existingOrganization() throws BaseException{
        Organization nonExistent = new Organization("Oh non non non");
        nonExistent.setId(-7);
        organizationService.update(nonExistent);
    }

}