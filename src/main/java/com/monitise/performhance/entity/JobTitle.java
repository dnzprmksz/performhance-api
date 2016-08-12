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

    // endregion

    // region Setters

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    // endregion

}