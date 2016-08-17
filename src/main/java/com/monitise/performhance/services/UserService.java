package com.monitise.performhance.services;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.access.annotation.Secured;
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
        List<User> list = (List<User>) userRepository.findAll();
        return list;
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

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public List<User> getByOrganizationId(int id) throws BaseException {
        List<User> users = userRepository.findByOrganizationId(id);
        if (users.size() == 0) {
            throw new BaseException(ResponseCode.USER_USERNAME_NOT_EXIST, "This organization has no employees.");
        }
        return users;
    }

    public List<Integer> getIdListByTeamId(int teamId) {
        List<Integer> idList = userRepository.findAllByTeamIdSelectUserId(teamId);
        return idList;
    }

    public List<Integer> getIdListByJobTitleId(int jobTitleId) {
        List<Integer> idList = userRepository.findAllByJobTitleIdSelectUserId(jobTitleId);
        return idList;
    }

    @Secured("ROLE_MANAGER")
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

        List<User> userList = userRepository.findAll(filter);

        return userList;
    }

    // TODO: Remove user from organization as well
    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public void remove(int id) throws BaseException {
        get(id);
        userRepository.delete(id);
    }

    public User update(User user) throws BaseException {
        ensureExistence(user);
        User userFromRepo = userRepository.save(user);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not update the user with given ID.");
        }
        return userFromRepo;
    }

    @Secured({"ROLE_MANAGER", "ROLE_ADMIN"})
    public User addEmployee(User user) throws BaseException {
        if (user.getRole() != Role.EMPLOYEE && user.getRole() != Role.TEAM_LEADER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non-employee user.");
        }
        User addedUser = addUser(user);
        organizationService.addEmployee(user.getOrganization(), addedUser);
        return addedUser;
    }

    @Secured("ROLE_ADMIN")
    public User addManager(User user) throws BaseException {
        if (user.getRole() != Role.MANAGER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non-manager user.");
        }
        return addUser(user);
    }

    // region Helper Methods

    private User addUser(User user) throws BaseException {
        String userName = user.getUsername();
        checkUserNameExistence(userName);
        User userFromRepo = userRepository.save(user);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given User.");
        }
        return userFromRepo;
    }

    private void checkUserNameExistence(String userName) throws BaseException {
        User user = userRepository.findByUsername(userName);
        if (user != null) {
            throw new BaseException(ResponseCode.USER_USERNAME_ALREADY_TAKEN, "That username is taken.");
        }
    }

    // Throws exception if the criteria DOES NOT EXIST.
    private void ensureExistence(User user) throws BaseException {
        User userFromRepo = userRepository.findOne(user.getId());
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.USER_ID_DOES_NOT_EXIST, "A user with given ID does not exist.");
        }
    }

    // endregion

}