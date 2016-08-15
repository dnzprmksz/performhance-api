package com.monitise.performhance.entity;

import java.util.Map;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    protected Review() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public User getReviewedEmployee() {
        return reviewedEmployee;
    }

    public void setReviewedEmployee(User reviewedEmployee) {
        this.reviewedEmployee = reviewedEmployee;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public Map<Criteria, Integer> getEvaluation() {
        return evaluation;
    }

    // endregion

    // region Setters

    public void setEvaluation(Map<Criteria, Integer> evaluation) {
        this.evaluation = evaluation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    // endregion

}