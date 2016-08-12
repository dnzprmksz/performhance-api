package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class SimplifiedTeamResponse {

    private int id;
    private String name;
    private String leaderName;

    public SimplifiedTeamResponse(Team team) {
        id = team.getId();
        name = team.getName();
        if (team.getLeader() != null) {
            leaderName = team.getLeader().getName();
        }
    }

    public static List<SimplifiedTeamResponse> fromTeamList(List<Team> teamList) {
        List<SimplifiedTeamResponse> responseList = new ArrayList<>();
        for (Team team : teamList) {
            responseList.add(new SimplifiedTeamResponse(team));
        }
        return responseList;
    }

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLeaderName() {
        return leaderName;
    }

    // endregion

    // region Setters

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    // endregion

}