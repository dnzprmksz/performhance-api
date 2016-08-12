package com.monitise.performhance.services;

import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.api.model.BaseException;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.JobTitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobTitleService {

    @Autowired
    private JobTitleRepository jobTitleRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private SecurityHelper securityHelper;

    @Secured("ROLE_MANAGER")
    public JobTitle add(JobTitle jobTitle) throws BaseException {
        JobTitle jobTitleFromRepo = jobTitleRepository.save(jobTitle);
        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = organizationService.get(manager.getOrganization().getId());

        List<JobTitle> jobTitles = organization.getJobTitles();
        jobTitles.add(jobTitle);
        organization.setJobTitles(jobTitles);
        Organization organizationFromService = organizationService.update(organization);

        if (jobTitleFromRepo == null || organizationFromService == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given job title.");
        }

        return jobTitleFromRepo;
    }

    public List<JobTitle> getAll() {
        List<JobTitle> list = (List<JobTitle>) jobTitleRepository.findAll();
        return list;
    }

    public JobTitle get(int id) throws BaseException {
        JobTitle jobTitle = jobTitleRepository.findOne(id);

        if (jobTitle == null) {
            throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST, "An job title with given ID does not exist.");
        }

        return jobTitle;
    }

    public List<JobTitle> getListFilterByOrganizationId(int organizationId) throws BaseException {
        List<JobTitle> list = organizationService.get(organizationId).getJobTitles();
        return list;
    }

}