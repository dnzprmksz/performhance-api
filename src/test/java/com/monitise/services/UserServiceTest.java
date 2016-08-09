package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.api.model.BaseException;
import com.monitise.entity.Organization;
import com.monitise.entity.User;
import com.monitise.repositories.OrganizationRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Before
    public void setup() throws BaseException {

        Organization initial = new Organization("Monitise");
        organizationService.add(initial);
    }

    @After
    public void cleanup() {
        organizationRepository.deleteAll();
    }

    @Test
    public void add_uniqueEmployee_shouldAdd() throws BaseException {

        Organization organization = organizationService.getByName("Monitise");
        User user = new User("Deniz", "Parmaksız", organization);
        User userFromService = null;
        userFromService = userService.addEmployee(user);

        Assert.assertNotNull(userFromService);
        Assert.assertEquals(user.getName(), userFromService.getName());
        Assert.assertEquals(user.getSurname(), userFromService.getSurname());
    }

}
