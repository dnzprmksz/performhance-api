package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.entity.User;

import java.util.ArrayList;
import java.util.List;


public class UserResponse {

    private int id;
    private String name;
    private String surname;
    private JobTitle jobTitle;
    private Role role;
    private Organization organization;

    public UserResponse(User user) {
        id = user.getId();
        name = user.getName();
        surname = user.getSurname();
        jobTitle = user.getJobTitle();
        role = user.getRole();
        // TODO: Implement organization model.
        organization = user.getOrganization();
    }


    public static UserResponse fromUser(User user) {
        return new UserResponse(user);
    }

    public static List<UserResponse> fromUserList(List<User> users) {
        List<UserResponse> responses = new ArrayList<>();
        for(User user : users){
            UserResponse current = fromUser(user);
            responses.add(current);
        }
        return responses;
    }

}
