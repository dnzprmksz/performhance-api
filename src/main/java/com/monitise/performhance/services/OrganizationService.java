package com.monitise.performhance.services;

import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private TeamService teamService;

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization get(int id) throws BaseException {
        Organization organization = organizationRepository.findOne(id);
        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST,
                    "An organization with given ID does not exist.");
        }
        return organization;
    }

    public Organization getByName(String name) throws BaseException {
        Organization organization = organizationRepository.findByName(name);
        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_DOES_NOT_EXIST,
                    "An organization with given name does not exist.");
        }
        return organization;
    }

    public Organization add(Organization organization) throws BaseException {
        ensureUniqueness(organization.getName());
        Organization organizationFromRepo = organizationRepository.save(organization);
        if (organizationFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given organization.");
        }
        return organizationFromRepo;
    }

    public Organization update(Organization organization) throws BaseException {
        ensureExistence(organization.getId());
        return organizationRepository.save(organization);
    }

    public boolean isJobTitleDefined(int organizationId, int jobTitleId) throws BaseException {
        int jobTitleOrganizationId = jobTitleService.get(jobTitleId).getOrganization().getId();
        return organizationId == jobTitleOrganizationId;
    }

    public boolean isTeamIdDefined(int organizationId, int teamId) throws BaseException {
        int teamOrganizationId = teamService.get(teamId).getOrganization().getId();
        return organizationId == teamOrganizationId;
    }

    // TODO: TEST THIS METHOD
    public Organization addEmployee(int organizationId, int employeeId) throws BaseException {
        Organization organization = organizationRepository.findOne(organizationId);
        User employee = userService.get(employeeId);

        organization.getUsers().add(employee);
        organization.setNumberOfEmployees(organization.getNumberOfEmployees() + 1);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add employee");
        }
        return updatedOrganization;
    }

    // TODO: TEST THIS METHOD
    public Organization addTeam(int organizationId, Team team) throws BaseException {
        Organization organization = get(organizationId);
        organization.getTeams().add(team);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add this team to given organization.");
        }
        return updatedOrganization;
    }

    public Organization addCriteria(int organizationId, Criteria criteria) throws BaseException {
        Organization organization = get(organizationId);
        organization.getCriteriaList().add(criteria);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add given criteria to given organization.");
        }
        return updatedOrganization;
    }

    // TODO: TEST THIS METHOD
    public Organization setManager(int organizationId, int managerId) throws BaseException {
        Organization organization = organizationRepository.findOne(organizationId);
        User manager = userService.get(managerId);
        organization.setManager(manager);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to set manager to given organization.");
        }
        return updatedOrganization;
    }

    // region Helper Methods

    private void ensureUniqueness(String name) throws BaseException {
        Organization organization = organizationRepository.findByName(name);
        if (organization != null) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_EXISTS,
                    "An organization with given name already exists.");
        }
    }

    private void ensureExistence(int organizationId) throws BaseException {
        Organization organization = organizationRepository.findOne(organizationId);
        if (organization == null) {
            throw new BaseException(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST,
                    "An organization with given ID does not exist.");
        }
    }

    // endregion

}
