package com.monitise.performhance.services;

import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    public static final String UNDEFINED = "C8E7279CD035B23BB9C0F1F954DFF5B3";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User get(int id) throws BaseException {
        User user = userRepository.findOne(id);
        if (user == null) {
            throw new BaseException(ResponseCode.USER_ID_DOES_NOT_EXIST, "A user with given ID does not exist.");
        }
        return user;
    }

    public User getByUsername(String username) throws BaseException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new BaseException(ResponseCode.USER_USERNAME_NOT_EXIST, "A user with given username does not exist.");
        }
        return user;
    }

    public List<User> getByOrganizationId(int id) throws BaseException {
        return userRepository.findByOrganizationId(id);
    }

    public List<Integer> getIdListByTeamId(int teamId) {
        return userRepository.findAllByTeamIdSelectUserId(teamId);
    }

    public List<Integer> getIdListByJobTitleId(int jobTitleId) {
        return userRepository.findAllByJobTitleIdSelectUserId(jobTitleId);
    }

    public List<User> searchUsers(int organizationId, String teamId, String titleId, String name, String surname) {
        Specification<User> filter = User.organizationIdIs(organizationId);

        if (!UNDEFINED.equals(teamId)) {
            int intTeamId = Integer.parseInt(teamId);
            filter = Specifications.where(filter).and(User.teamIdIs(intTeamId));
        }
        if (!UNDEFINED.equals(titleId)) {
            int intTitleId = Integer.parseInt(titleId);
            filter = Specifications.where(filter).and(User.titleIdIs(intTitleId));
        }
        if (!UNDEFINED.equals(name)) {
            filter = Specifications.where(filter).and(User.nameContains(name));
        }
        if (!UNDEFINED.equals(surname)) {
            filter = Specifications.where(filter).and(User.surnameContains(surname));
        }

        return userRepository.findAll(filter);
    }

    // TODO: Remove user from organization as well
    public void remove(int id) throws BaseException {
        ensureExistence(id);
        userRepository.delete(id);
    }

    public User update(User user) throws BaseException {
        ensureExistence(user.getId());
        User userFromRepo = userRepository.save(user);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not update the user with given ID.");
        }
        return userFromRepo;
    }

    public User addEmployee(User user) throws BaseException {
        if (user.getRole() != Role.EMPLOYEE && user.getRole() != Role.TEAM_LEADER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non-employee user.");
        }
        User addedUser = addUser(user);
        organizationService.addEmployee(user.getOrganization().getId(), addedUser.getId());
        return addedUser;
    }

    public User addManager(User manager) throws BaseException {
        if (manager.getRole() != Role.MANAGER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non-manager user.");
        }
        User addedManager = addUser(manager);
        int organizationId = manager.getOrganization().getId();
        int managerId = manager.getId();
        organizationService.addEmployee(organizationId, managerId);
        organizationService.setManager(organizationId, managerId);
        return addedManager;
    }

    // region Helper Methods

    private User addUser(User user) throws BaseException {
        ensureUsernameUniqueness(user.getUsername());
        User userFromRepo = userRepository.save(user);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given User.");
        }
        return userFromRepo;
    }

    private void ensureUsernameUniqueness(String userName) throws BaseException {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            throw new BaseException(ResponseCode.USER_USERNAME_ALREADY_TAKEN, "That username is taken.");
        }
    }

    private void ensureExistence(int userId) throws BaseException {
        User userFromRepo = userRepository.findOne(userId);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.USER_ID_DOES_NOT_EXIST, "A user with given ID does not exist.");
        }
    }

    // endregion

}