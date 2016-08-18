package com.monitise.performhance.api.model;

import java.util.Map;

public class EmployeeScoreResponse {

    private String reviewedEmployeeName;
    private int reviewCount;
    private Map<String, Integer> averageCriteriaScores;

    public EmployeeScoreResponse(String reviewedEmployee, int reviewCount, Map<String, Integer> criteriaScores) {
        this.reviewedEmployeeName = reviewedEmployee;
        this.reviewCount = reviewCount;
        this.averageCriteriaScores = criteriaScores;
    }

    public String getReviewedEmployeeName() {
        return reviewedEmployeeName;
    }

    public void setReviewedEmployeeName(String reviewedEmployeeName) {
        this.reviewedEmployeeName = reviewedEmployeeName;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Map<String, Integer> getAverageCriteriaScores() {
        return averageCriteriaScores;
    }

    public void setAverageCriteriaScores(Map<String, Integer> averageCriteriaScores) {
        this.averageCriteriaScores = averageCriteriaScores;
    }
}
