package com.monitise.performhance.api.model;

import java.util.Map;

public class AddReviewRequest {
    private int reviewedEmployeeId;
    private int reviewerId;
    // Key: criteriaId, Value: evaluationValue
    private Map<Integer, Integer> evaluationIdMap;
    private String comment;

    public AddReviewRequest() {
    }

    // region Getters & Setters

    public int getReviewedEmployeeId() {
        return reviewedEmployeeId;
    }

    public void setReviewedEmployeeId(int reviewedEmployeeId) {
        this.reviewedEmployeeId = reviewedEmployeeId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public Map<Integer, Integer> getEvaluationIdMap() {
        return evaluationIdMap;
    }

    public void setEvaluationIdMap(Map<Integer, Integer> evaluationIdMap) {
        this.evaluationIdMap = evaluationIdMap;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // endregion

}
