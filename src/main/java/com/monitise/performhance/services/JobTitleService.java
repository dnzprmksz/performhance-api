package com.monitise.performhance.services;

import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
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

    public JobTitle add(JobTitle jobTitle) throws BaseException {
        JobTitle jobTitleFromRepo = jobTitleRepository.save(jobTitle);
        Organization organization = organizationService.get(jobTitleFromRepo.getOrganization().getId());

        List<JobTitle> organizationJobTitles = organization.getJobTitles();
        organizationJobTitles.add(jobTitle);
        organization.setJobTitles(organizationJobTitles);
        Organization organizationFromService = organizationService.update(organization);
        if (organizationFromService == null) {
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
            throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST,
                    "A job title with given ID does not exist.");
        }
        return jobTitle;
    }

    public List<JobTitle> getListFilterByOrganizationId(int organizationId) throws BaseException {
        return organizationService.get(organizationId).getJobTitles();
    }

}