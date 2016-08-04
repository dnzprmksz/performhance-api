package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.models.BaseException;
import com.monitise.models.Organization;
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

        Organization initial = new Organization("Pozitron");
        Organization organizationFromRepo = null;
        organizationService.add(initial);
    }

    @After
    public void cleanup() {
        organizationRepository.deleteAll();
    }

    @Test
    public void getByUsername_existingName() throws BaseException {

        Organization organizationFromService = organizationService.getByName("Pozitron");
        Assert.assertNotNull(organizationFromService);
        Assert.assertEquals("Pozitron", organizationFromService.getName());
    }

    @Test
    public void getByUsername_nonExistingName(){
        Organization organization = null;
        try {
            organization = organizationService.getByName("Monitise");
        } catch (BaseException e) {
            Assert.assertNull(organization);
        }
    }

    @Test
    public void add_uniqueOrganization_shouldAdd() throws BaseException {
        Organization organization = new Organization("Monitise");
        Organization organizationFromRepo = null;
        organizationFromRepo = organizationService.add(organization);

        Assert.assertNotNull(organizationFromRepo);
        Assert.assertEquals(organization.getName(), organizationFromRepo.getName());
    }

    @Test
    public void add_existingOrganization_shouldNotAdd() throws BaseException {

        Organization organizationFromRepo = null;
        Organization organization = new Organization("Pozitron");
        Assert.assertNotNull(organizationService.getByName("Pozitron"));

        try {
            organizationFromRepo = organizationService.add(organization);
        } catch (Exception exception) {
            Assert.assertNull(organizationFromRepo);
        }
    }

}