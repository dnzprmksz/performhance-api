package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Organization;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedOrganization {

    private int id;
    private String name;
    private int numberOfEmployees;

    public SimplifiedOrganization(Organization organization) {
        id = organization.getId();
        name = organization.getName();
        numberOfEmployees = organization.getNumberOfEmployees();
    }

    public static SimplifiedOrganization fromOrganization(Organization organization) {
        if (organization == null) {
            return null;
        }
        return new SimplifiedOrganization(organization);
    }

    public static List<SimplifiedOrganization> fromList(List<Organization> organizations) {
        List<SimplifiedOrganization> responseList = new ArrayList<>();
        for (Organization organization : organizations) {
            responseList.add(new SimplifiedOrganization(organization));
        }
        return responseList;
    }

    // region Getters & Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(int numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    // endregion

}