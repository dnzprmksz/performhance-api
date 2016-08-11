package com.monitise.api;

import com.monitise.api.model.AddReviewRequest;
import com.monitise.api.model.BaseException;
import com.monitise.api.model.Response;
import com.monitise.api.model.ResponseCode;
import com.monitise.api.model.ReviewResponse;
import com.monitise.entity.Criteria;
import com.monitise.entity.Review;
import com.monitise.entity.User;
import com.monitise.services.CriteriaService;
import com.monitise.services.ReviewService;
import com.monitise.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private CriteriaService criteriaService;

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

    private void validate(AddReviewRequest reviewRequest) throws BaseException {
        User reviewedUser = userService.get(reviewRequest.getReviewedEmployeeId());
        User reviewer = userService.get(reviewRequest.getReviewerId());
        checkUserRelationship(reviewedUser, reviewer);
        List<Criteria> criteriaList = reviewedUser.getCriteriaList();
        int userCriteriaCount = criteriaList.size();
        int requestCriteriaCount = 0;

        for (Map.Entry entry : buildCriteriaMapFromIdMap(reviewRequest.getEvaluationIdMap()).entrySet()) {
            Criteria criteria = (Criteria) entry.getKey();
            int value = (Integer) entry.getValue();

            // Remove this criteria from user's criteria list. At the end the list should be empty, so that all criteria are evaluated.
            if (!criteriaList.contains(criteria)) {
                throw new BaseException(ResponseCode.CRITERIA_DOES_NOT_EXIST_IN_USER, "Criteria (" + criteria.getId() + ") does not belong to this employee.");
            }
            requestCriteriaCount++;

            if (value < 0 || value > 100) {
                throw new BaseException(ResponseCode.REVIEW_EVALUATION_VALUE_INVALID, "Evaluation values must be between 0 and 100.");
            }
        }

        if (userCriteriaCount != requestCriteriaCount) {
            throw new BaseException(ResponseCode.REVIEW_USER_CRITERIA_LIST_NOT_SATISFIED, "You must evaluate all criteria of the employee.");
        }
    }

    private void checkUserRelationship(User first, User second) throws BaseException {
        if (first.getTeam() != second.getTeam()) {
            throw new BaseException(ResponseCode.REVIEW_USER_IN_DIFFERENT_TEAM, "Reviewed employee and reviewer are in different teams.");
        } else if (first.getId() == second.getId()) {
            throw new BaseException(ResponseCode.REVIEW_SAME_USER, "You cannot review yourself.");
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

}