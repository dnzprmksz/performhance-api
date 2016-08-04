package com.monitise.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany
    private List<User> members;
    @OneToOne
    private User leader;

    protected Team() {}

    public boolean addMember(User user) {
        return members.add(user);
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getMembers() {
        return members;
    }

    public User getLeader() {
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

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    // endregion

}