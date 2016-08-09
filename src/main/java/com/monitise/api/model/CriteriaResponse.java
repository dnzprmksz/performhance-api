package com.monitise.api.model;

import com.monitise.entity.Criteria;

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
        CriteriaResponse criteriaResponse = new CriteriaResponse(criteria.getId(), criteria.getCriteria());
        return criteriaResponse;
    }

    public static List<CriteriaResponse> fromList(List<Criteria> criteriaList) {
        List<CriteriaResponse> criteriaResponseList = new ArrayList<>();
        CriteriaResponse criteriaResponse;

        for (Criteria criteria : criteriaList) {
            criteriaResponse = new CriteriaResponse(criteria.getId(), criteria.getCriteria());
            criteriaResponseList.add(criteriaResponse);
        }

        return criteriaResponseList;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getCriteria() {
        return criteria;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    // endregion

}