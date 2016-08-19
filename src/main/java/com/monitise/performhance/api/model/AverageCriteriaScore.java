package com.monitise.performhance.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class AverageCriteriaScore {

    private String criteriaName;
    private int averageScore;
    private int reviewTimes;
    @JsonIgnore
    private int totalScore;

    public AverageCriteriaScore(String criteriaName) {
        this.criteriaName = criteriaName;
        averageScore = 0;
        reviewTimes = 0;
        totalScore = 0;
    }

    public String getCriteriaName() {
        return criteriaName;
    }

    public void setCriteriaName(String criteriaName) {
        this.criteriaName = criteriaName;
    }

    public int getReviewTimes() {
        return reviewTimes;
    }

    public void setReviewTimes(int reviewTimes) {
        this.reviewTimes = reviewTimes;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
