package com.monitise.performhance.services;

import com.monitise.performhance.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.repositories.TeamRepository;
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

    public List<Team> searchTeams(int organizationId, String teamId, String teamName) {
        Specification<Team> filter = Team.organizationIdIs(organizationId);
        if (!UNDEFINED.equals(teamId)) {
            int intTeamId = Integer.parseInt(teamId);
            filter = Specifications.where(filter).and(Team.teamIdIs(intTeamId));
        }
        if (!UNDEFINED.equals(teamName)) {
            filter = Specifications.where(filter).and(Team.teamNameContains(teamName));
        }

        List<Team> teamList = teamRepository.findAll(filter);

        return teamList;
    }

    @Secured("ROLE_MANAGER")
    public List<Team> getListFilterByOrganizationId(int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Team> list = teamRepository.findByOrganizationId(organizationId);
        if (list == null) {
            return new ArrayList<>();
        }
        return list;
    }

    @Secured("ROLE_MANAGER")
    public Team add(Team team) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(team.getOrganization().getId());
        Team teamFromRepo = teamRepository.save(team);

        if (teamFromRepo == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given team.");
        }
        return teamFromRepo;
    }

    @Secured("ROLE_MANAGER")
    public Team assingEmployeeToTeam(User user, Team team) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(team.getOrganization().getId());
        securityHelper.checkUserOrganizationAuthorization(user.getOrganization().getId());
        Team teamFromRepo = teamRepository.findOne(team.getId());

        if (teamFromRepo == null) {
            throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST,
                    "Could not add given user to team, since the team does not exist.");
        }

        List<User> members = teamFromRepo.getMembers();
        members.add(user);
        teamFromRepo.setMembers(members);
        Team updatedTeam = teamRepository.save(teamFromRepo);
        return updatedTeam;
    }

}