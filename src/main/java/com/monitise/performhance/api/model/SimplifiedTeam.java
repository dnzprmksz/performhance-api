package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedTeam {

    private int id;
    private String name;
    private String leaderName;

    public SimplifiedTeam(Team team) {
        id = team.getId();
        name = team.getName();
        if (team.getLeader() != null) {
            User leader = team.getLeader();
            leaderName = leader.getName() + " " + leader.getSurname();
        }
    }

    public static List<SimplifiedTeam> fromList(List<Team> teamList) {
        if (teamList == null) {
            return null;
        }
        List<SimplifiedTeam> responseList = new ArrayList<>();
        for (Team team : teamList) {
            responseList.add(new SimplifiedTeam(team));
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

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    // endregion

}