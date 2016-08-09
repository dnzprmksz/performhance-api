package com.monitise.api.model;

import com.monitise.entity.Organization;
import com.monitise.entity.Team;

import java.util.ArrayList;
import java.util.List;

public class TeamResponse {

    private int id;
    private String name;
    private List<SimplifiedUser> members;
    private String organizationName;
    private UserResponse leader;

    public TeamResponse(Team team) {
        id = team.getId();
        name = team.getName();
        members = SimplifiedUser.fromUserList(team.getMembers());
        organizationName = team.getOrganization().getName();
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

    // region Getters

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<SimplifiedUser> getMembers() {
        return members;
    }

    public String getOrganization() {
        return organizationName;
    }

    public UserResponse getLeader() {
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

    public void setMembers(List<SimplifiedUser> members) {
        this.members = members;
    }

    public void setOrganization(String organizationName) {
        this.organizationName= organizationName;
    }

    public void setLeader(UserResponse leader) {
        this.leader = leader;
    }

    // endregion

}
