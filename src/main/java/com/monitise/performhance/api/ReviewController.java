package com.monitise.performhance.api;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.AddReviewRequest;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.ReviewResponse;
import com.monitise.performhance.api.model.SimplifiedReview;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.ReviewService;
import com.monitise.performhance.services.UserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    // region Dependencies

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private RelationshipHelper relationshipHelper;

    // endregion

    @Secured({"ROLE_MANAGER", "ROLE_TEAM_LEADER"})
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<SimplifiedReview>> getAll() throws BaseException {
        List<Review> list = null;
        User user = securityHelper.getAuthenticatedUser();
        if (securityHelper.isAuthenticatedUserManager()) {
            list = reviewService.getAllFilterByOrganizationId(user.getOrganization().getId());
        } else if (securityHelper.isAuthenticatedUserTeamLeader()) {
            list = reviewService.getAllFilterByTeamId(user.getTeam().getId());
        }
        List<SimplifiedReview> simplifiedReviews = SimplifiedReview.fromList(list);

        Response<List<SimplifiedReview>> response = new Response<>();
        response.setData(simplifiedReviews);
        response.setSuccess(true);
        return response;
    }

    @Secured({"ROLE_EMPLOYEE", "ROLE_TEAM_LEADER"})
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<ReviewResponse> add(@RequestBody AddReviewRequest reviewRequest) throws BaseException {
        validate(reviewRequest);
        Review review = new Review(userService.get(reviewRequest.getReviewedEmployeeId()),
                userService.get(reviewRequest.getReviewerId()),
                buildCriteriaMapFromIdMap(reviewRequest.getEvaluationIdMap()),
                reviewRequest.getComment());

        Review reviewFromService = reviewService.add(review);
        ReviewResponse reviewResponse = new ReviewResponse(reviewFromService);

        Response<ReviewResponse> response = new Response<>();
        response.setData(reviewResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured({"ROLE_MANAGER", "ROLE_TEAM_LEADER"})
    @RequestMapping(value = "/{reviewId}", method = RequestMethod.GET)
    public Response<ReviewResponse> get(@PathVariable int reviewId) throws BaseException {
        Review review = reviewService.get(reviewId);
        if (securityHelper.isAuthenticatedUserManager()) {
            relationshipHelper.checkManagerReviewRelationship(review);
        } else if (securityHelper.isAuthenticatedUserTeamLeader()) {
            relationshipHelper.checkTeamLeaderReviewRelationship(review);
        }
        ReviewResponse reviewResponse = new ReviewResponse(review);
        Response<ReviewResponse> response = new Response<>();
        response.setData(reviewResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{reviewId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int reviewId) throws BaseException {
        Review review = reviewService.get(reviewId);
        relationshipHelper.checkManagerReviewRelationship(review);
        reviewService.remove(reviewId);

        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    // region Helper Methods

    private void validate(AddReviewRequest reviewRequest) throws BaseException {
        User reviewedUser = userService.get(reviewRequest.getReviewedEmployeeId());
        User reviewer = userService.get(reviewRequest.getReviewerId());
        relationshipHelper.checkEmployeeRelationship(reviewedUser, reviewer);

        List<Criteria> criteriaList = reviewedUser.getCriteriaList();
        int userCriteriaCount = criteriaList.size();
        int requestCriteriaCount = 0;

        for (Map.Entry entry : buildCriteriaMapFromIdMap(reviewRequest.getEvaluationIdMap()).entrySet()) {
            Criteria criteria = (Criteria) entry.getKey();
            int value = (Integer) entry.getValue();

            // Remove this criteria from user's criteria list.
            // At the end the list should be empty, so that all criteria are evaluated.
            if (!criteriaList.contains(criteria)) {
                throw new BaseException(ResponseCode.CRITERIA_DOES_NOT_EXIST_IN_USER,
                        "Criteria (" + criteria.getId() + ") does not belong to this employee.");
            }
            requestCriteriaCount++;

            if (value < 0 || value > 100) {
                throw new BaseException(ResponseCode.REVIEW_EVALUATION_VALUE_INVALID,
                        "Evaluation values must be between 0 and 100.");
            }
        }

        if (userCriteriaCount != requestCriteriaCount) {
            throw new BaseException(ResponseCode.REVIEW_USER_CRITERIA_LIST_NOT_SATISFIED,
                    "You must evaluate all criteria of the employee.");
        }
    }

    private Map<Criteria, Integer> buildCriteriaMapFromIdMap(Map<Integer, Integer> map) throws BaseException {
        Map<Criteria, Integer> hashMap = new HashMap<>();
        for (Map.Entry entry : map.entrySet()) {
            int criteriaId = Integer.parseInt(entry.getKey().toString());
            int evaluationValue = Integer.parseInt(entry.getValue().toString());
            Criteria criteria = criteriaService.get(criteriaId);
            hashMap.put(criteria, evaluationValue);
        }
        return hashMap;
    }

    // endregion

}
