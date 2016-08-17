package com.monitise.performhance.api.model;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class SimplifiedUserTest {
    private static boolean init = false;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Test
    public void fromUser() {
        User user = userRepository.findOne(1);
        SimplifiedUser simplified = SimplifiedUser.fromUser(user);
        isEqualUserSimplifiedUser(user, simplified);
    }

    @Test
    public void fromUserList() {
        List<User> userList = userRepository.findByOrganizationId(1);
        List<SimplifiedUser> simplifiedList = SimplifiedUser.fromUserList(userList);

        Assert.assertEquals(2, simplifiedList.size());
        Assert.assertEquals(2, userList.size());
        isEqualUserSimplifiedUser(userList.get(0), simplifiedList.get(0));
        isEqualUserSimplifiedUser(userList.get(1), simplifiedList.get(1));
    }


    private void isEqualUserSimplifiedUser(User user, SimplifiedUser simplified) {
        Assert.assertEquals(user.getId(), simplified.getId());
        Assert.assertEquals(user.getRole(), simplified.getRole());
        Assert.assertEquals(user.getName(), simplified.getName());
        Assert.assertEquals(user.getSurname(), simplified.getSurname());
        Assert.assertEquals(user.getJobTitle(), simplified.getJobTitle());
    }
}
