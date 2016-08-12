package com.monitise.performhance.api.model;

import com.monitise.performhance.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamResponse {

    private int id;
    private int organizationId;
    private String name;
    private TeamUserResponse leader;
    private List<TeamUserResponse> members;

    public TeamResponse(Team team) {
        id = team.getId();
        name = team.getName();
        members = TeamUserResponse.fromUserList(team.getMembers());
        organizationId = team.getOrganization().getId();
        if (team.getLeader() != null) {
            leader = new TeamUserResponse(team.getLeader());
        }
    }

    public static TeamResponse fromTeam(Team team) {
        if (team == null) {
            return null;
        }
        return new TeamResponse(team);
    }

    public static List<TeamResponse> fromTeamList(List<Team> teams) {
        if (teams == null) {
            return null;
        }
        List<TeamResponse> responses = new ArrayList<>();
        for (Team team : teams) {
            TeamResponse current = fromTeam(team);
            responses.add(current);
        }
        return responses;
    }

    // region Getters

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

    public List<TeamUserResponse> getMembers() {
        return members;
    }

    // endregion

    // region Setters

    public void setMembers(List<TeamUserResponse> members) {
        this.members = members;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public TeamUserResponse getLeader() {
        return leader;
    }

    public void setLeader(TeamUserResponse leader) {
        this.leader = leader;
    }

    // endregion

}
