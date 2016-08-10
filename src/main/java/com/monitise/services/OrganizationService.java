package com.monitise.services;

import com.monitise.api.model.BaseException;
import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.api.model.ResponseCode;
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
        // Check if the organization name is unique.
        Organization shouldBeNull = organizationRepository.findByName(organization.getName());
        if(shouldBeNull != null) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_EXISTS,
                    "An organization with given name already exists.");
        }
        Organization organizationFromRepo = organizationRepository.save(organization);
        // Check if the given organization is added correctly.
        if (organizationFromRepo == null || !(organizationFromRepo.getName().equals(organization.getName()))) {
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

    public boolean isJobTitleDefined(Organization organization, int titleId){
        List<JobTitle> titleList = organization.getJobTitles();
        for(JobTitle i : titleList) {
            if( i.getId() == titleId ) {
                return true;
            }
        }
        return false;
    }
}