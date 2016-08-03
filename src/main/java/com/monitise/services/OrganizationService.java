package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.Organization;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public Iterable<Organization> getAll() {

        // Find all organizations in repository.
        Iterable<Organization> list = organizationRepository.findAll();
        return list;
    }

    public Organization get(int id) throws BaseException {

        // Find the organization with given ID in repository.
        Organization organization = organizationRepository.findOne(id);

        // Check the success of the action and throw an exception if the action fails.
        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST, "An organization with given ID does not exist.");
        }

        return organization;
    }

    public Organization getByName(String name) throws BaseException {

        // Find the organization with given name in repository.
        Organization organization = organizationRepository.findByName(name);

        // Check the success of the action and throw an exception if the action fails.
        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_DOES_NOT_EXIST, "An organization with given name does not exist.");
        }

        return organization;
    }

    public Organization add(Organization organization) throws BaseException {

        // Add the given organization to the repository.
        Organization organizationFromRepo = organizationRepository.save(organization);
        boolean isAdded = false;

        // Check if the given organization is added correctly.
        if (organizationFromRepo != null && (organizationFromRepo.getName().equals(organization.getName()))) {
            isAdded = true;
        }

        // Check the success of the action and throw an exception if the action fails.
        if (!isAdded) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given organization.");
        }

        return organizationFromRepo;
    }

}