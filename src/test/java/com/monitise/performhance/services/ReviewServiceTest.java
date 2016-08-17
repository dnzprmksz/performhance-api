package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.BaseException;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.repositories.CriteriaRepository;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.TeamRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AppConfig.class)
@WebAppConfiguration
public class ReviewServiceTest {

    private User reviewedUser;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ReviewService reviewService;

    @Test
    public void add_reviewerFirstTime_shouldAdd() throws BaseException {
        Organization organization = organizationService.getByName("Monitise");
        User reviewer = new User("Orbay", "Altuntoprak", organization);
        Map<Criteria, Integer> evaluation = new HashMap<>();

        for (Criteria criteria : reviewedUser.getCriteriaList()) {
            int value = (int) (Math.random() * 30) + 70;
            evaluation.put(criteria, value);
        }
        Review review = new Review(reviewedUser, reviewer, evaluation, "");
        Review reviewFromService = reviewService.add(review);

        Assert.assertNotNull(reviewFromService);
    }
}