package com.monitise.api.model;

import com.monitise.entity.JobTitle;
import com.monitise.entity.Organization;
import com.monitise.entity.Role;
import com.monitise.entity.User;


public class UserResponse {

    private String name;
    private String surname;
    private JobTitle jobTitle;
    private Role role;
    private Organization organization;

    public UserResponse(User user){
        name = user.getName();
        surname = user.getSurname();
        jobTitle = user.getJobTitle();
        role = user.getRole();
    }
}
