package com.monitise.performhance.services;

import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.UpdateOrganizationRequest;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.Util;
import com.monitise.performhance.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private ReviewService reviewService;

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
        Organization organizationFromRepo = organizationRepository.save(organization);
        if (organizationFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not update given organization.");
        }
        return organizationFromRepo;
    }

    public Organization updateFromRequest(UpdateOrganizationRequest updateOrganizationRequest, int organizationId)
            throws BaseException {
        checkUpdateRequest(updateOrganizationRequest);
        ensureUniqueness(updateOrganizationRequest.getName());
        Organization organization = get(organizationId);
        organization.setName(updateOrganizationRequest.getName());

        return update(organization);
    }

    public boolean isJobTitleDefined(int organizationId, int jobTitleId) throws BaseException {
        int jobTitleOrganizationId = jobTitleService.get(jobTitleId).getOrganization().getId();
        return organizationId == jobTitleOrganizationId;
    }

    public boolean isTeamIdDefined(int organizationId, int teamId) throws BaseException {
        int teamOrganizationId = teamService.get(teamId).getOrganization().getId();
        return organizationId == teamOrganizationId;
    }

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

    public void remove(int organizationId) throws BaseException {
        ensureExistence(organizationId);
        removeJobTitles(organizationId);
        removeReviews(organizationId);
        removeAllCriteria(organizationId);
        removeTeams(organizationId);
        removeUsers(organizationId);
        organizationRepository.delete(organizationId);
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

    private void removeJobTitles(int organizationId) throws BaseException {
        removeJobTitleOfAllEmployees(organizationId);
        Organization organization = get(organizationId);

        List<JobTitle> jobTitles = organization.getJobTitles();
        int jobTitleCount = jobTitles.size();
        int jobTitleId = 0;
        for (int i = 0; i < jobTitleCount; i++) {
            jobTitleId = jobTitles.get(0).getId();
            jobTitleService.remove(jobTitleId);
        }
    }

    private void removeJobTitleOfAllEmployees(int organizationId) throws BaseException {
        Organization organization = get(organizationId);
        for (User employee : organization.getUsers()) {
            employee.setJobTitle(null);
            userService.update(employee);
        }
    }

    private void removeAllCriteria(int organizationId) throws BaseException {
        removeCriteriaOfAllEmployees(organizationId);
        Organization organization = get(organizationId);

        List<Criteria> criteriaList = organization.getCriteriaList();
        int criteriaCount = criteriaList.size();
        int criteriaId = 0;
        for (int i = 0; i < criteriaCount; i++) {
            criteriaId = criteriaList.get(0).getId();
            criteriaService.remove(criteriaId);
        }
        update(organization);
    }

    private void removeCriteriaOfAllEmployees(int organizationId) throws BaseException {
        Organization organization = get(organizationId);
        for (User employee : organization.getUsers()) {
            removeAllCriteriaOfAnEmployee(employee.getId());
        }
    }

    private void removeAllCriteriaOfAnEmployee(int userId) throws BaseException {
        User user = userService.get(userId);
        List<Criteria> criteriaList = user.getCriteriaList();
        int criteriaCount = criteriaList.size();
        int criteriaId = 0;
        for (int i = 0; i < criteriaCount; i++) {
            criteriaId = criteriaList.get(0).getId();
            criteriaService.removeCriteriaFromUserById(criteriaId, userId);
        }
    }

    private void removeReviews(int organizationId) throws BaseException {
        removeReviewsOfAllEmployees(organizationId);
        List<Review> reviews = reviewService.getAllFilterByOrganizationId(organizationId);
        for (Review review : reviews) {
            reviewService.remove(review.getId());
        }
    }

    private void removeReviewsOfAllEmployees(int organizationId) throws BaseException {
        Organization organization = get(organizationId);
        for (User employee : organization.getUsers()) {
            removeReviewsOfAnEmployee(employee.getId());
        }
    }

    private void removeReviewsOfAnEmployee(int userId) throws BaseException {
        User user = userService.get(userId);
        List<Review> reviews = user.getReviews();
        int reviewCount = reviews.size();

        for (int i = 0; i < reviewCount; i++) {
            reviews.remove(0);
        }
        userService.update(user);
    }

    private void removeUsers(int organizationId) throws BaseException {
        Organization organization = get(organizationId);
        List<User> users = organization.getUsers();
        int userCount = users.size();
        for (int i = 0; i < userCount; i++) {
            User user = users.get(0);
            userService.remove(user.getId());
        }
    }

    private void removeTeams(int organizationId) throws BaseException {
        Organization organization = get(organizationId);
        List<Team> teams = organization.getTeams();
        int teamCount = teams.size();
        for (int i = 0; i < teamCount; i++) {
            Team team = teams.get(0);
            teamService.remove(team.getId());
        }
    }

    private void checkUpdateRequest(UpdateOrganizationRequest request) throws BaseException {
        String name = request.getName();
        if (Util.isNullOrEmpty(name)) {
            throw new BaseException(ResponseCode.ORGANIZATION_NAME_INVALID,
                    "Empty organization name is not allowed.");
        }
    }

    // endregion

}
