package com.monitise.services;

import com.monitise.AppConfig;
import com.monitise.api.model.BaseException;
import com.monitise.entity.Criteria;
import com.monitise.entity.Organization;
import com.monitise.entity.Review;
import com.monitise.entity.Team;
import com.monitise.entity.User;
import com.monitise.repositories.CriteriaRepository;
import com.monitise.repositories.OrganizationRepository;
import com.monitise.repositories.TeamRepository;
import com.monitise.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
public class ReviewServiceTest {

    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private CriteriaRepository criteriaRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private TeamService teamService;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewService reviewService;

    private static User reviewedUser;

    @Before
    @WithMockUser(roles = {"MANAGER"})
    public void setup() throws BaseException {
        Organization organization = organizationRepository.save(new Organization("Monitise"));
        Team team = teamRepository.save(new Team("İşCep", organization));
        reviewedUser = new User("Deniz", "Parmaksız", organization);
        User user = userRepository.save(reviewedUser);
        teamService.assingEmployeeToTeam(user, team);
        Criteria criteria1 = criteriaRepository.save(new Criteria("Code coverage", organization));
        Criteria criteria2 = criteriaRepository.save(new Criteria("8 hours of daily work", organization));
        Criteria criteria3 = criteriaRepository.save(new Criteria("Teamwork", organization));
        criteriaService.assignCriteriaToUserById(criteria1, user.getId());
        criteriaService.assignCriteriaToUserById(criteria2, user.getId());
        criteriaService.assignCriteriaToUserById(criteria3, user.getId());
    }

    @After
    public void cleanup() {
        criteriaRepository.deleteAll();
        userRepository.deleteAll();
        teamRepository.deleteAll();
        organizationRepository.deleteAll();
    }

    @Test
    public void add_reviewerFirstTime_shouldAdd() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        User reviewer = new User("Orbay", "Altuntoprak", organization);
        Map<Criteria, Integer> evaluation = new HashMap<>();

        for (Criteria criteria : reviewedUser.getCriteriaList()) {
            int value = (int)(Math.random() * 30) + 70;
            evaluation.put(criteria, value);
        }
        Review review = new Review(reviewedUser, reviewer, evaluation);
        Review reviewFromService = reviewService.add(review);

        Assert.assertNotNull(reviewFromService);
    }
}