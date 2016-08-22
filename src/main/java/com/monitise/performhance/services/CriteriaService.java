package com.monitise.performhance.services;

import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.repositories.CriteriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CriteriaService {

    @Autowired
    private CriteriaRepository criteriaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private RelationshipHelper relationshipHelper;

    public List<Criteria> getAll() {
        return criteriaRepository.findAll();
    }

    public List<Criteria> getAllFilterByOrganizationId(int organizationId) throws BaseException {
        return criteriaRepository.findByOrganizationId(organizationId);
    }

    public Criteria get(int id) throws BaseException {
        Criteria criteria = criteriaRepository.findOne(id);
        if (criteria == null) {
            throw new BaseException(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST,
                    "A criteria with given ID does not exist.");
        }
        return criteria;
    }

    public Criteria add(Criteria criteria) throws BaseException {
        validate(criteria);
        checkExistenceInOrganization(criteria);
        Criteria criteriaFromRepo = criteriaRepository.save(criteria);
        if (criteriaFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given criteria.");
        }
        addCriteriaToOrganization(criteriaFromRepo.getOrganization().getId(), criteria);
        return criteriaFromRepo;
    }

    public void remove(int criteriaId) throws BaseException {
        ensureExistence(criteriaId);
        criteriaRepository.delete(criteriaId);
    }

    public Criteria update(Criteria criteria) throws BaseException {
        validate(criteria);
        ensureExistence(criteria.getId());
        return criteriaRepository.save(criteria);
    }

    public User assignCriteriaToUserById(int criteriaId, int userId) throws BaseException {
        User user = userService.get(userId);
        Criteria criteria = get(criteriaId);
        checkExistenceInUser(user, criteria);
        List<Criteria> criteriaList = user.getCriteriaList();
        criteriaList.add(criteria);
        user.setCriteriaList(criteriaList);
        return userService.update(user);
    }

    // Finds all the users of a given job title and assigns the criteria to each of them.
    // The criteria has already been assigned to some users, nothing happens.
    // Returns a list of user's ids who already have the criteria.
    public ArrayList<Integer> assignCriteriaToJobTitle(int criteriaId, int jobTitleId) throws BaseException {
        relationshipHelper.ensureJobTitleCriteriaRelationship(jobTitleId, criteriaId);
        List<Integer> userIdList = userService.getIdListByJobTitleId(jobTitleId);
        return assignCriteriaToUserList(criteriaId, userIdList);
    }

    // Finds all the users of a given team and assigns the criteria to each of them.
    // The criteria has already been assigned to some users, nothing happens.
    // Returns a list of user's ids who already have the criteria.
    public ArrayList<Integer> assignCriteriaToTeam(int criteriaId, int teamId) throws BaseException {
        relationshipHelper.ensureTeamCriteriaRelationship(teamId, criteriaId);
        List<Integer> userIdList = userService.getIdListByTeamId(teamId);
        return assignCriteriaToUserList(criteriaId, userIdList);
    }

    // return List of users who already have the criteria.
    public ArrayList<Integer> assignCriteriaToUserList(int criteriaId, List<Integer> userIdList) throws BaseException {
        int organizationId = get(criteriaId).getOrganization().getId();
        relationshipHelper.ensureOrganizationUserListRelationship(organizationId, userIdList);
        ArrayList<Integer> existingUserList = new ArrayList<>();
        // Add criteria to users. If user already has this criteria, add his/her ID to the list.
        for (int userId : userIdList) {
            boolean userAlreadyHasCriteria = checkUserExistenceAndAssignCriteriaById(userId, criteriaId);
            if (userAlreadyHasCriteria) {
                existingUserList.add(userId);
            }
        }
        return existingUserList;
    }

    public void removeCriteriaFromUserById(int criteriaId, int userId) throws BaseException {
        User user = userService.get(userId);
        Criteria criteria = get(criteriaId);
        List<Criteria> criteriaList = user.getCriteriaList();
        if (!criteriaList.contains(criteria)) {
            throw new BaseException(ResponseCode.CRITERIA_DOES_NOT_EXIST_IN_USER,
                    "Given user does not have this criteria.");
        }
        criteriaList.remove(criteria);
        user.setCriteriaList(criteriaList);
        User userFromService = userService.update(user);
        if (userFromService == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not remove the given criteria for the given user.");
        }
    }

    // region Helper Methods

    /**
     * @return The existence of the user.
     */
    private boolean checkUserExistenceAndAssignCriteriaById(int userId, int criteriaId) throws BaseException {
        try {
            assignCriteriaToUserById(criteriaId, userId);
        } catch (BaseException exception) {
            if (exception.getCode() == ResponseCode.CRITERIA_EXISTS_IN_USER) {
                return true;
            } else {
                throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given criteria to given user list.");
            }
        }
        return false;
    }

    // Throws exception if the criteria DOES NOT EXIST.
    private void ensureExistence(int criteriaId) throws BaseException {
        Criteria criteriaFromRepo = criteriaRepository.findOne(criteriaId);
        if (criteriaFromRepo == null) {
            throw new BaseException(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST,
                    "A criteria with given ID does not exist.");
        }
    }

    // Throws exception if the criteria DOES EXIST.
    private void checkExistenceInOrganization(Criteria criteria) throws BaseException {
        Criteria criteriaFromRepo = criteriaRepository.findByOrganizationAndCriteria(criteria.getOrganization(),
                criteria.getCriteria());
        if (criteriaFromRepo != null) {
            throw new BaseException(ResponseCode.CRITERIA_EXISTS_IN_ORGANIZATION,
                    "Given criteria exists in the given organization.");
        }
    }

    private void validate(Criteria criteria) throws BaseException {
        String criteriaName = criteria.getCriteria();
        if (criteriaName == null || criteriaName.trim() == "") {
            throw new BaseException(ResponseCode.CRITERIA_EMPTY, "Empty criteria is not allowed.");
        } else if (criteria.getOrganization() == null) {
            throw new BaseException(ResponseCode.CRITERIA_EMPTY_ORGANIZATION,
                    "Criteria must belong to an organization.");
        }
    }

    private void checkExistenceInUser(User user, Criteria criteria) throws BaseException {
        if (user.getCriteriaList().contains(criteria)) {
            throw new BaseException(ResponseCode.CRITERIA_EXISTS_IN_USER, "Given user already has this criteria.");
        }
    }

    private void addCriteriaToOrganization(int organizationId, Criteria criteria) throws BaseException {
        Organization organization = organizationService.get(organizationId);
        organization.getCriteriaList().add(criteria);
        Organization updatedOrganization = organizationService.update(organization);
        if (updatedOrganization == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Failed to add given criteria to given organization.");
        }
    }

    // endregion

}