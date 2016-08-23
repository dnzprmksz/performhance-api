package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
public class ReviewServiceTest {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void get_existingReview() throws BaseException {
        Review review = reviewService.get(4);
        User pelya = userRepository.findOne(4);
        boolean reviewCheck()
    }

    private void reviewCheck(int loopCounter, Review review, int id,
              int organizationId, int teamId, int reviewedId, int reviewerId, List<Criteria> criteriaList) {
        Assert.assertEquals(id, review.getId());
        Assert.assertEquals(organizationId, review.getOrganization().getId());
        Assert.assertEquals(teamId, review.getTeam().getId());
        Assert.assertEquals(reviewedId, review.getReviewedEmployee().getId());
        Assert.assertEquals(reviewerId, review.getReviewer().getId());

        Map<Criteria,Integer> evaluations = review.getEvaluation();
        Assert.assertEquals(criteriaList.size(), evaluations.size());
        for (Criteria criteria : criteriaList) {
            Assert.assertTrue(evaluations.containsKey(criteria));
        }
    }
}