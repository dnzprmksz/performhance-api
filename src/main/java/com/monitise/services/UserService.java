package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.Response;
import com.monitise.models.Role;
import com.monitise.models.User;
import com.monitise.models.ResponseCode;
import com.monitise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
            throw new BaseException(ResponseCode.USER_ID_DOES_NOT_EXIST, "An user with given ID does not exist.");
        }

        return user;
    }

    public User getByUsername(String username) throws BaseException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new BaseException(ResponseCode.USER_USERNAME_NOT_EXIST, "An user with given username does not exist.");
        }

        return user;
    }

    @Secured("ROLE_MANAGER")
    public User add(User user) throws BaseException {

        if (user.getRole() != Role.EMPLOYEE || user.getRole() != Role.TEAM_LEADER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non employee/team-leader user.");
        }

        User userFromRepo = userRepository.save(user);

        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given User.");
        }

        return userFromRepo;
    }

    @Secured("ROLE_ADMIN")
    public User addManager(User user) throws BaseException {

        if (user.getRole() != Role.MANAGER) {
            throw new BaseException(ResponseCode.USER_ROLE_INCORRECT, "Cannot add non-manager user.");
        }

        User userFromRepo = userRepository.save(user);

        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given User.");
        }

        return userFromRepo;
    }

    public User getAuthenticatedUser() throws BaseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = auth.getName();
        User authenticatedUser = getByUsername(authenticatedUsername);
        return authenticatedUser;
    }

}