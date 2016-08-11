package com.monitise.api.model;

import com.monitise.entity.Criteria;
import com.monitise.entity.Review;

import java.util.HashMap;
import java.util.Map;

public class ReviewResponse {

    private String reviewedEmployeeName;
    private String reviewerName;
    private Map<String, Integer> evaluation;
    private String comment;

    public ReviewResponse(Review review) {
        reviewedEmployeeName = review.getReviewedEmployee().getName() + " " + review.getReviewedEmployee().getSurname();
        reviewerName = review.getReviewer().getName() + " " + review.getReviewer().getSurname();

        HashMap<String, Integer> hashMap = new HashMap<>();
        for (Map.Entry entry : review.getEvaluation().entrySet()) {
            String criteriaName = ((Criteria)entry.getKey()).getCriteria();
            int value = ((Integer)entry.getValue());
            hashMap.put(criteriaName, value);
        }
        evaluation = hashMap;
        comment = review.getComment();
    }

    // region Getters

    public String getReviewedEmployeeName() {
        return reviewedEmployeeName;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public Map<String, Integer> getEvaluation() {
        return evaluation;
    }

    public String getComment() {
        return comment;
    }

    // endregion

    // region Setters

    public void setReviewedEmployeeName(String reviewedEmployeeName) {
        this.reviewedEmployeeName = reviewedEmployeeName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public void setEvaluation(Map<String, Integer> evaluation) {
        this.evaluation = evaluation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // endregion

}