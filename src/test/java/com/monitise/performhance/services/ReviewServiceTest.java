package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.CustomMatcher;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
@Transactional
public class ReviewServiceTest {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserRepository userRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void get_existingReview() throws BaseException {
        Review review = reviewService.get(4);
        User pelya = userRepository.findOne(4);
        reviewCheck(review, 4, 1, 1, 4, 3, pelya.getCriteriaList());
    }

    @Test
    public void get_nonExistingReview() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.REVIEW_ID_DOES_NOT_EXIST));
        reviewService.get(45654);
    }

    @Test
    public void getAllFilterByOrganizationId() throws BaseException {
        List<Review> list = reviewService.getAllFilterByOrganizationId(2);
        User bilge = userRepository.findOne(7);
        User yigit = userRepository.findOne(8);

        Assert.assertEquals(2, list.size());
        reviewCheck(list.get(0), 5, 2, 2, 7, 8, bilge.getCriteriaList());
        reviewCheck(list.get(1), 6, 2, 2, 8, 9, yigit.getCriteriaList());
    }

    @Test
    public void getAllFilterByOrganizationId_nonExistingOrganization() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST));
        reviewService.getAllFilterByOrganizationId(210012);
    }

    @Test
    public void getAllFilterByTeamId_noReviewsInTeam() throws BaseException {
        List<Review> reviews = reviewService.getAllFilterByTeamId(3);
        Assert.assertNotNull(reviews);
        Assert.assertEquals(0, reviews.size());
    }

    @Test
    public void getAllFilterByTeamId() throws BaseException {
        List<Review> list = reviewService.getAllFilterByTeamId(2);
        User bilge = userRepository.findOne(7);
        User yigit = userRepository.findOne(8);

        Assert.assertNotNull(list);
        Assert.assertEquals(2, list.size());

        reviewCheck(list.get(0), 5, 2, 2, 7, 8, bilge.getCriteriaList());
        reviewCheck(list.get(1), 6, 2, 2, 8, 9, yigit.getCriteriaList());
    }

    @Test
    public void getAllFilterByTeamId_nonExistingTeam() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.TEAM_ID_DOES_NOT_EXIST));
        reviewService.getAllFilterByTeamId(519915);
    }

    @Test
    public void remove() throws BaseException {
        reviewService.remove(3);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.REVIEW_ID_DOES_NOT_EXIST));
        reviewService.get(3);
    }

    @Test
    public void remove_nonExistingId() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.REVIEW_ID_DOES_NOT_EXIST));
        reviewService.remove(1230321);
    }

    @Test
    public void add() throws BaseException {
        User pelin = userRepository.findOne(2);
        User pelya = userRepository.findOne(4);
        Map<Criteria, Integer> evaluationMap = new HashMap();
        List<Criteria> pelyaCriteria = pelya.getCriteriaList();
        for (int i = 0; i < pelyaCriteria.size(); ++i) {
            evaluationMap.put(pelyaCriteria.get(i), (i * i * i * i * i + 2345) % 100);
        }

        Review review = new Review(pelya, pelin, evaluationMap, "");
        final int TEST_REVIEW_ID = reviewService.add(review).getId();
        Review fetched = reviewService.get(TEST_REVIEW_ID);
        reviewCheck(fetched, TEST_REVIEW_ID, 1, 1, 4, 2, pelyaCriteria);

    }

    private void reviewCheck(Review review, int id,
              int organizationId, int teamId, int reviewedId, int reviewerId, List<Criteria> criteriaList) {
        Assert.assertEquals(id, review.getId());
        Assert.assertEquals(organizationId, review.getOrganization().getId());
        Assert.assertEquals(teamId, review.getTeam().getId());
        Assert.assertEquals(reviewedId, review.getReviewedEmployee().getId());
        Assert.assertEquals(reviewerId, review.getReviewer().getId());

        Assert.assertNotNull(criteriaList);
        Map<Criteria,Integer> evaluations = review.getEvaluation();
        Assert.assertEquals(criteriaList.size(), evaluations.size());
        for (Criteria criteria : criteriaList) {
            Assert.assertTrue(evaluations.containsKey(criteria));
        }
    }
}