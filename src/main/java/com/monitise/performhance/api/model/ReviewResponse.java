package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReviewResponse {

    private int id;
    private String reviewedEmployeeName;
    private String reviewerName;
    private Map<String, Integer> evaluation;
    private String comment;

    public ReviewResponse(Review review) {
        id = review.getId();
        reviewedEmployeeName = review.getReviewedEmployee().getName() + " " + review.getReviewedEmployee().getSurname();

        if (review.getReviewer() != null) {
            reviewerName = review.getReviewer().getName() + " " + review.getReviewer().getSurname();
        } else {
            reviewerName = "N/A";
        }

        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Map.Entry entry : review.getEvaluation().entrySet()) {
            String criteriaName = ((Criteria) entry.getKey()).getCriteria();
            int value = ((Integer) entry.getValue());
            hashMap.put(criteriaName, value);
        }
        evaluation = hashMap;
        comment = review.getComment();
    }

    public static List<ReviewResponse> fromReviewList(List<Review> list) {
        List<ReviewResponse> responseList = new ArrayList();
        if (list == null) {
            return responseList;
        }
        for (Review review : list) {
            responseList.add(new ReviewResponse(review));
        }
        return responseList;
    }

    // region Getters

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

    // endregion

    // region Setters

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public Map<String, Integer> getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(Map<String, Integer> evaluation) {
        this.evaluation = evaluation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // endregion

}