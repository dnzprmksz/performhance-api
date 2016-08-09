package com.monitise.api.model;

import com.monitise.entity.Organization;
import com.monitise.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamResponse {

    private int id;
    private String name;
    private List<UserResponse> members;
    private Organization organization;
    private UserResponse leader;

    public TeamResponse(Team team) {
        id = team.getId();
        name = team.getName();
        members = UserResponse.fromUserList(team.getMembers());
        // TODO: Implement organization model
        organization = team.getOrganization();
        leader = UserResponse.fromUser(team.getLeader());
    }

    public static TeamResponse fromTeam(Team team) {
        return new TeamResponse(team);
    }

    public static List<TeamResponse> fromTeamList(List<Team> teams){
        List<TeamResponse> responses = new ArrayList<>();
        for(Team team : teams){
            TeamResponse current = fromTeam(team);
            responses.add(current);
        }
        return responses;
    }

}
