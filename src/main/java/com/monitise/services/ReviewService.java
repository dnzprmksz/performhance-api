package com.monitise.services;

import com.monitise.api.model.BaseException;
import com.monitise.api.model.ResponseCode;
import com.monitise.entity.Review;
import com.monitise.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review add(Review review) throws BaseException {
        Review reviewFromRepo = reviewRepository.save(review);
        if (reviewFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add the given review.");
        }
        return reviewFromRepo;
    }

}