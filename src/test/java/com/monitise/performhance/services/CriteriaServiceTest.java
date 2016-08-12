package com.monitise.performhance.services;

import com.monitise.AppConfig;
import com.monitise.performhance.api.model.BaseException;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.repositories.CriteriaRepository;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
public class CriteriaServiceTest {

    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private CriteriaRepository criteriaRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Before
    public void setup() throws BaseException {
        Organization organization = new Organization("Monitise");
        Organization alternativeOrganization = new Organization("Pozitron");
        Criteria criteria = new Criteria("Code coverage", organization);

        organizationRepository.save(organization);
        organizationRepository.save(alternativeOrganization);
        criteriaRepository.save(criteria);
    }

    @After
    public void cleanup() {
        criteriaRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void add_nonExistingCriteria_shouldAdd() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        Criteria criteria = new Criteria("At least 8 hours of daily work", organization);
        Criteria criteriaFromService = criteriaService.add(criteria);

        Assert.assertNotNull(criteriaFromService);
        Assert.assertEquals("At least 8 hours of daily work", criteriaFromService.getCriteria());
        Assert.assertEquals("Monitise", criteriaFromService.getOrganization().getName());
    }

    @Test
    public void add_existingCriteriaDifferentOrganization_shouldAdd() throws BaseException {
        Organization organization = organizationService.getByName("Pozitron");
        Criteria criteria = new Criteria("Code coverage", organization);
        Criteria criteriaFromService = criteriaService.add(criteria);

        Assert.assertNotNull(criteriaFromService);
        Assert.assertEquals("Code coverage", criteriaFromService.getCriteria());
        Assert.assertEquals("Pozitron", criteriaFromService.getOrganization().getName());
    }

    @Test
    public void add_existingCriteriaSameOrganization_shouldNotAdd() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        Criteria criteria = new Criteria("Code coverage", organization);
        Criteria criteriaFromService = null;

        try {
            criteriaFromService = criteriaService.add(criteria);
        } catch (BaseException exception) {
            Assert.assertNull(criteriaFromService);
        }
        Assert.assertNull(criteriaFromService);
    }

    @Test
    public void add_emptyCriteria_shouldNotAdd() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        Criteria criteria = new Criteria("", organization);
        Criteria criteriaFromService = null;

        try {
            criteriaFromService = criteriaService.add(criteria);
        } catch (BaseException exception) {
            Assert.assertNull(criteriaFromService);
        }
        Assert.assertNull(criteriaFromService);
    }

}