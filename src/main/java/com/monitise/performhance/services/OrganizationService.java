package com.monitise.performhance.services;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserService userService;

    public List<Organization> getAll() {
        List<Organization> list = (List<Organization>) organizationRepository.findAll();
        return list;
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
        // Check if the organization name is unique.
        Organization shouldBeNull = organizationRepository.findByName(organization.getName());
        if (shouldBeNull != null) {
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

    public boolean isJobTitleDefined(Organization organization, int titleId) {
        List<JobTitle> titleList = organization.getJobTitles();
        for (JobTitle jobTitle : titleList) {
            if (jobTitle.getId() == titleId) {
                return true;
            }
        }
        return false;
    }

    public boolean isTeamIdDefined(Organization organization, int teamId) {
        List<Team> teams = organization.getTeams();
        for (Team team : teams) {
            if (team.getId() == teamId) {
                return true;
            }
        }
        return false;
    }

    // TODO: TEST THIS METHOD
    public Organization addEmployee(int organizationId, int employeeId) throws BaseException {
        Organization organization = organizationRepository.findOne(organizationId);
        User employee = userService.get(employeeId);
        List<User> userList = organization.getUsers();

        // Add the employee & increment numberOfEmployees field.
        userList.add(employee);
        organization.setUsers(userList);
        organization.setNumberOfEmployees(organization.getNumberOfEmployees() + 1);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add employee");
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

    // TODO: TEST THIS METHOD
    public Organization addTeam(int organizationId, Team team) throws BaseException {
        Organization organization = get(organizationId);
        List<Team> teamList = organization.getTeams();
        teamList.add(team);
        organization.setTeams(teamList);
        Organization updatedOrganization = organizationRepository.save(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add this team to given organization.");
        }
        return updatedOrganization;
    }

    public Organization addCriteria(int organizationId, Criteria criteria) throws BaseException {
        Organization organization = get(organizationId);
        List<Criteria> criteriaList = organization.getC
    }



}