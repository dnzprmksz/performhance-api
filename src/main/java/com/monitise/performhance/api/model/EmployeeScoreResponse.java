package com.monitise.performhance.api.model;

import java.util.List;

public class EmployeeScoreResponse {

    private String reviewedEmployeeName;
    private int reviewCount;
    private List<AverageCriteriaScore> averageCriteriaScores;
    private List<String> comments;

    public EmployeeScoreResponse(String reviewedEmployee, int reviewCount, List<AverageCriteriaScore> criteriaScores,
                                 List<String> comments) {
        this.reviewedEmployeeName = reviewedEmployee;
        this.reviewCount = reviewCount;
        this.averageCriteriaScores = criteriaScores;
        this.comments = comments;
    }

    // region Getters & Setters

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

    public List<AverageCriteriaScore> getAverageCriteriaScores() {
        return averageCriteriaScores;
    }

    public void setAverageCriteriaScores(List<AverageCriteriaScore> averageCriteriaScores) {
        this.averageCriteriaScores = averageCriteriaScores;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    // endregion

}