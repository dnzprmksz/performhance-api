package com.monitise.services;

import com.monitise.entity.BaseException;
import com.monitise.api.model.Role;
import com.monitise.entity.User;
import com.monitise.entity.ResponseCode;
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
            throw new BaseException(ResponseCode.USER_ID_DOES_NOT_EXIST, "An user with given ID does not exist.");
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
        List<User> users = userRepository.findByOrganizationId(id);

        if ( users.size() == 0 ) {
            throw new BaseException(ResponseCode.USER_USERNAME_NOT_EXIST, "This organization has no employees.");
        }

        return users;
    }

    public void remove(int id){
        userRepository.delete(id);
    }



    @Secured("ROLE_MANAGER")
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
        User userFromRepo = userRepository.save(user);
        if (userFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given User.");
        }
        return userFromRepo;
    }

    // endregion

}