package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Criteria;

import java.util.ArrayList;
import java.util.List;

public class CriteriaResponse {

    private int id;
    private String criteria;

    public CriteriaResponse(int id, String criteria) {
        this.id = id;
        this.criteria = criteria;
    }

    public static CriteriaResponse fromCriteria(Criteria criteria) {
        return new CriteriaResponse(criteria.getId(), criteria.getCriteria());
    }

    public static List<CriteriaResponse> fromList(List<Criteria> criteriaList) {
        if (criteriaList == null) {
            return null;
        }
        List<CriteriaResponse> criteriaResponseList = new ArrayList<>();
        for (Criteria criteria : criteriaList) {
            criteriaResponseList.add(fromCriteria(criteria));
        }
        return criteriaResponseList;
    }

    // region Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    // endregion

}