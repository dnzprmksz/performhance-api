package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Review;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedReview {

    private int id;
    private String reviewedEmployeeName;
    private String reviewerName;
    private String comment;

    public SimplifiedReview(Review review) {
        id = review.getId();
        reviewedEmployeeName = review.getReviewedEmployee().getName();
        reviewerName = review.getReviewer().getName() + " " + review.getReviewer().getSurname();
        comment = review.getComment();
    }

    public static List<SimplifiedReview> fromList(List<Review> reviewList) {
        List<SimplifiedReview> list = new ArrayList<>();
        for (Review review : reviewList) {
            list.add(new SimplifiedReview(review));
        }
        return list;
    }

    // region Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReviewedEmployeeName() {
        return reviewedEmployeeName;
    }

    public void setReviewedEmployeeName(String reviewedEmployeeName) {
        this.reviewedEmployeeName = reviewedEmployeeName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // endregion

}
