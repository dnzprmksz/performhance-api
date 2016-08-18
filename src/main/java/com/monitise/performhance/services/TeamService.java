package com.monitise.performhance.services;

import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.repositories.TeamRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeamService {

    public static final String UNDEFINED = "c8e7279cd035b23bb9c0f1f954dff5b3";

    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationService organizationService;

    public List<Team> getAll() {
        List<Team> list = (List<Team>) teamRepository.findAll();
        return list;
    }

    public Team get(int id) throws BaseException {
        Team team = teamRepository.findOne(id);
        if (team == null) {
            throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST, "A team with given ID does not exist.");
        }
        return team;
    }

    public List<Team> searchTeams(int organizationId, String teamName) {
        Specification<Team> filter = Team.organizationIdIs(organizationId);
        if (!UNDEFINED.equals(teamName)) {
            filter = Specifications.where(filter).and(Team.teamNameContains(teamName));
        }
        List<Team> teamList = teamRepository.findAll(filter);
        return teamList;
    }

    public List<Team> getListFilterByOrganizationId(int organizationId) throws BaseException {
        securityHelper.checkAuthentication(organizationId);
        List<Team> list = teamRepository.findByOrganizationId(organizationId);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    public Team add(Team team) throws BaseException {
        int organizationId = team.getOrganization().getId();
        securityHelper.checkAuthentication(organizationId);
        Team teamFromRepo = teamRepository.save(team);

        if (teamFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given team.");
        }
        organizationService.addTeam(organizationId, teamFromRepo);
        return teamFromRepo;
    }

    public Team assignEmployeeToTeam(int userId, int teamId) throws BaseException {
        Team team = teamRepository.findOne(teamId);
        User user = userRepository.findOne(userId);

        List<User> members = team.getMembers();
        members.add(user);
        team.setMembers(members);
        Team updatedTeam = teamRepository.save(team);
        user.setTeam(team);
        userRepository.save(user);
        return updatedTeam;
    }

    public Team assignLeaderToTeam(int leaderId, int teamId) throws BaseException {
        if(!isLeaderAMemberOfTheTeam(teamId, leaderId)) {
            assignEmployeeToTeam(leaderId, teamId);
        }
        Team team = teamRepository.findOne(teamId);
        User leader = userRepository.findOne(leaderId);
        team.setLeader(leader);
        Team updatedTeam = teamRepository.save(team);
        return updatedTeam;
    }

    private boolean isLeaderAMemberOfTheTeam(int teamId, int leaderId) {
        Team team = teamRepository.findOne(teamId);
        User leader = userRepository.findOne(leaderId);
        List<User> members = team.getMembers();
        for(User member : members) {
            if(member.getId() == leader.getId()) {
                return true;
            }
        }
        return false;
    }

}
