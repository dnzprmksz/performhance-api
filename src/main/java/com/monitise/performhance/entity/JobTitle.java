package com.monitise.performhance.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class JobTitle {

    @Id
    @GeneratedValue
    private int id;
    private String title;

    protected JobTitle() {
    }

    public JobTitle(String title) {
        this.title = title;
    }

    // region Getters

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    // endregion

    // region Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // endregion

}