package com.monitise.services;

import com.monitise.helpers.SecurityHelper;
import com.monitise.entity.BaseException;
import com.monitise.entity.User;
import com.monitise.entity.ResponseCode;
import com.monitise.entity.Team;
import com.monitise.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

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

    @Secured("ROLE_MANAGER")
    public List<Team> getListFilterByOrganizationId(int organizationId) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(organizationId);
        List<Team> list = teamRepository.findByOrganizationId(organizationId);
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
    public void assingEmployeeToTeam(User user, Team team) throws BaseException {
        securityHelper.checkUserOrganizationAuthorization(team.getOrganization().getId());
        securityHelper.checkUserOrganizationAuthorization(user.getOrganization().getId());
        Team teamFromRepo = teamRepository.findOne(team.getId());

        if (teamFromRepo == null) {
            throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST, "Could not add given user to team, since the team does not exist.");
        }

        List<User> members = teamFromRepo.getMembers();
        members.add(user);
        teamFromRepo.setMembers(members);
        teamRepository.save(teamFromRepo);
    }

}