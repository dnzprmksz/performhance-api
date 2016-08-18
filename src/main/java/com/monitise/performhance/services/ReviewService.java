package com.monitise.performhance.services;

import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.ReviewRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Review> getAll() {
        return reviewRepository.findAll();
    }

    public List<Review> getAllFilterByOrganizationId(int organizationId) {
        return reviewRepository.findByOrganizationId(organizationId);
    }

    public List<Review> getAllFilterByTeamId(int teamId) {
        return reviewRepository.findByTeamId(teamId);
    }

    public Review get(int reviewId) throws BaseException {
        Review review = reviewRepository.findOne(reviewId);
        if (review == null) {
            throw new BaseException(ResponseCode.REVIEW_ID_DOES_NOT_EXIST, "A review with given ID does not exist.");
        }
        return review;
    }

    public Review add(Review review) throws BaseException {
        Review reviewFromRepo = reviewRepository.save(review);
        if (reviewFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given review.");
        }
        User employee = review.getReviewedEmployee();
        employee.getReviews().add(review);
        userRepository.save(employee);
        return reviewFromRepo;
    }

    public Review update(Review review) throws BaseException {
        ensureExistence(review.getId());
        return reviewRepository.save(review);
    }

    public void remove(int reviewId) throws BaseException {
        ensureExistence(reviewId);
        reviewRepository.delete(reviewId);
    }

    // region Helper Methods

    private void ensureExistence(int reviewId) throws BaseException {
        Review reviewFromRepo = reviewRepository.findOne(reviewId);
        if (reviewFromRepo == null) {
            throw new BaseException(ResponseCode.REVIEW_ID_DOES_NOT_EXIST, "A review with given ID does not exist.");
        }
    }

    // endregion

}
