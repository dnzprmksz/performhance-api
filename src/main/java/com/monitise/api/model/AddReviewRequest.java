package com.monitise.api.model;

import java.util.Map;

public class AddReviewRequest {
    private int reviewedEmployeeId;
    private int reviewerId;
    private Map<Integer, Integer> evaluationIdMap; // Key: criteriaId, Value: evaluationValue
    private String comment;

    public AddReviewRequest() {}

    // region Getters

    public int getReviewedEmployeeId() {
        return reviewedEmployeeId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public Map<Integer, Integer> getEvaluationIdMap() {
        return evaluationIdMap;
    }

    public String getComment() {
        return comment;
    }

    // endregion

    // region Setters

    public void setReviewedEmployeeId(int reviewedEmployeeId) {
        this.reviewedEmployeeId = reviewedEmployeeId;
    }

    public void setReviewerId(int reviewerId) {
        this.reviewerId = reviewerId;
    }

    public void setEvaluationIdMap(Map<Integer, Integer> evaluationIdMap) {
        this.evaluationIdMap = evaluationIdMap;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // endregion

}