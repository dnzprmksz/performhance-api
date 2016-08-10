package com.monitise.services;

import com.fasterxml.jackson.databind.deser.Deserializers;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.Role;
import com.monitise.entity.User;
import com.monitise.api.model.ResponseCode;
import com.monitise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


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
        return addUser(user);
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
        if (user != null)
            throw new BaseException(ResponseCode.USER_USERNAME_ALREADY_TAKEN, "That username is taken.");
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