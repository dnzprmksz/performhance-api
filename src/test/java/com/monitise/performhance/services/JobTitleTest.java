package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.CustomMatcher;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
public class JobTitleTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Test
    public void add_nonExisting_shouldAdd() throws BaseException {
        Organization organization = organizationRepository.findOne(1);
        JobTitle jobTitle = new JobTitle("Senior Software Engineer", organization);
        JobTitle jobTitleFromService = jobTitleService.add(jobTitle);

        Assert.assertNotNull(jobTitleFromService);
        Assert.assertEquals(jobTitle.getTitle(), jobTitleFromService.getTitle());
        Assert.assertEquals(jobTitle.getOrganization(), jobTitleFromService.getOrganization());
    }

    @Test
    public void add_existingInDifferentOrganization_shouldAdd() throws BaseException {
        Organization organization = organizationRepository.findOne(1);
        JobTitle jobTitle = new JobTitle("Intern", organization);  // Intern exists in Monitise (2)
        JobTitle jobTitleFromService = jobTitleService.add(jobTitle);

        Assert.assertNotNull(jobTitleFromService);
        Assert.assertEquals(jobTitle.getTitle(), jobTitleFromService.getTitle());
        Assert.assertEquals(jobTitle.getOrganization(), jobTitleFromService.getOrganization());
    }

    @Test
    public void add_existingInSameOrganization_shouldNotAdd() throws BaseException {
        Organization organization = organizationRepository.findOne(2);
        JobTitle jobTitle = new JobTitle("ios dev", organization);  // ios dev exists in Monitise (2)
        thrown.expect(CustomMatcher.hasCode(ResponseCode.JOB_TITLE_EXISTS_IN_ORGANIZATION));
        jobTitleService.add(jobTitle);
    }

    @Test
    public void update_validData_shouldUpdate() throws BaseException {
        JobTitle jobTitle = jobTitleService.get(1);
        jobTitle.setTitle("Junior Developer");
        JobTitle jobTitleFromService = jobTitleService.update(jobTitle);

        Assert.assertNotNull(jobTitleFromService);
        Assert.assertEquals(jobTitle.getTitle(), jobTitleFromService.getTitle());
    }

    @Test
    public void remove_notUsedJobTitle_shouldRemove() throws BaseException {
        jobTitleService.remove(6);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST));
        jobTitleService.get(6);
    }

    @Test
    public void remove_usedJobTitle_shouldNotRemove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.JOB_TITLE_IN_USE));
        jobTitleService.remove(1);
    }

}
