package com.monitise.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany
    private List<Employee> members;
    private Employee leader;

    protected Team() {}

    public boolean addMember(Employee employee) {
        return members.add(employee);
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Employee> getMembers() {
        return members;
    }

    public Employee getLeader() {
        return leader;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMembers(List<Employee> members) {
        this.members = members;
    }

    public void setLeader(Employee leader) {
        this.leader = leader;
    }

    // endregion

}