package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.models.BaseException;
import com.monitise.models.Organization;
import org.junit.Assert;
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

    @Test
    public void getByUsername_ExistingName() {
        Organization initial = new Organization("Pozitron");
        Organization organization = null;

        // Initialize database with an object.
        try {
            organizationService.add(initial);
        } catch (BaseException e) {}

        // Add same object.
        try {
            organization = organizationService.getByName("Pozitron");
        } catch (BaseException e) {}

        Assert.assertNotNull(organization);
        Assert.assertEquals("Pozitron", organization.getName());
    }

    @Test
    public void getByUsername_NonExistingName() {
        Organization organization = null;

        try {
            organization = organizationService.getByName("Monitise");
        } catch (BaseException e) {}

        Assert.assertNull(organization);
    }

    @Test
    public void add_UniqueOrganization_ShouldAdd() {
        Organization organization = new Organization("Monitise");
        Organization organizationFromRepo = null;

        try {
            organizationFromRepo = organizationService.add(organization);
        } catch (BaseException e) {}

        Assert.assertNotNull(organizationFromRepo);
        Assert.assertEquals(organization.getName(), organizationFromRepo.getName());
    }

    @Test
    public void add_ExistingOrganization_ShouldNotAdd() {
        Organization organization = new Organization("Pozitron");
        Organization organizationFromRepo = null;

        try {
            Assert.assertNotNull(organizationService.getByName("Pozitron"));
            organizationFromRepo = organizationService.add(organization);
        } catch (Exception e) {}

        Assert.assertNull(organizationFromRepo);
    }

}