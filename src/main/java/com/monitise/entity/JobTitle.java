package com.monitise.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import java.io.Serializable;
import java.util.List;

@Entity
public class JobTitle implements Serializable {

    @Id
    @GeneratedValue
    private int id;
    private String title;
    @ManyToMany
    private List<Criteria> criteriaList;

    protected JobTitle() {}

    public JobTitle(String title) {
        this.title = title;
    }

    // region Getters

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public List<Criteria> getCriteriaList() {
        return criteriaList;
    }

    // endregion

    // region Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCriteriaList(List<Criteria> criteriaList) {
        this.criteriaList = criteriaList;
    }

    // endregion

}