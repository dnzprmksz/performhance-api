package com.monitise.entity;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Map;

@Entity
public class Review {

    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    private User reviewedEmployee;
    @ManyToOne
    private User reviewer;
    @ElementCollection
    private Map<Criteria, Integer> evaluation;
    private String comment;
    @ManyToOne
    private Organization organization;
    @ManyToOne
    private Team team;

    protected Review() {}

    public Review(User reviewedEmployee, User reviewer, Map<Criteria, Integer> evaluation, String comment) {
        this.reviewedEmployee = reviewedEmployee;
        this.reviewer = reviewer;
        this.evaluation = evaluation;
        organization = reviewedEmployee.getOrganization();
        team = reviewedEmployee.getTeam();
        this.comment = comment;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public User getReviewedEmployee() {
        return reviewedEmployee;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Map<Criteria, Integer> getEvaluation() {
        return evaluation;
    }

    public String getComment() {
        return comment;
    }

    public Organization getOrganization() {
        return organization;
    }

    public Team getTeam() {
        return team;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setReviewedEmployee(User reviewedEmployee) {
        this.reviewedEmployee = reviewedEmployee;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public void setEvaluation(Map<Criteria, Integer> evaluation) {
        this.evaluation = evaluation;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    // endregion

}