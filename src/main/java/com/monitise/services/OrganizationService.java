package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.JobTitle;
import com.monitise.models.Organization;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<Organization> getAll() {

        List<Organization> list = (List<Organization>) organizationRepository.findAll();
        return list;
    }

    public Organization get(int id) throws BaseException {

        Organization organization = organizationRepository.findOne(id);

        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST, "An organization with given ID does not exist.");
        }

        return organization;
    }

    public Organization getByName(String name) throws BaseException {

        Organization organization = organizationRepository.findByName(name);

        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_DOES_NOT_EXIST, "An organization with given name does not exist.");
        }

        return organization;
    }

    public Organization add(Organization organization) throws BaseException {

        Organization organizationFromRepo = organizationRepository.save(organization);
        boolean isAdded = false;

        // Check if the given organization is added correctly.
        if (organizationFromRepo != null && (organizationFromRepo.getName().equals(organization.getName()))) {
            isAdded = true;
        }

        if (!isAdded) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given organization.");
        }

        return organizationFromRepo;
    }

    public Organization update(Organization organization) throws BaseException {

        // Check if organization exists in repository. If DNE an exception will be thrown by getId().
        get(organization.getId());
        Organization organizationFromRepo = organizationRepository.save(organization);
        return organizationFromRepo;
    }

    public boolean isJobTitleDefined(Organization organization, JobTitle title){

        List<JobTitle> titleList = organization.getJobTitles();

        for(JobTitle i : titleList) {
            if( i.getTitle().equals(title.getTitle()) ) {
                return true;
            }
        }
        return false;

    }
}