package com.monitise.performhance.services;

import com.monitise.performhance.api.model.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getAll() {
        List<Review> list = (List<Review>) reviewRepository.findAll();
        return list;
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
        return reviewFromRepo;
    }

    public Review update(Review review) throws BaseException {
        ensureExistence(review.getId());
        Review reviewFromRepo = reviewRepository.save(review);
        return reviewFromRepo;
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
