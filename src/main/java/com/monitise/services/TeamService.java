package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.User;
import com.monitise.models.ResponseCode;
import com.monitise.models.Team;
import com.monitise.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Secured("ROLE_MANAGER")
    public Team add(Team team) throws BaseException {

        // Add given team to repository.
        Team teamFromRepo = teamRepository.save(team);

        // Check the success of the action and throw an exception if the action fails.
        if (team == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given team.");
        }

        return  teamFromRepo;
    }

    @Secured("ROLE_MANAGER")
    public void assingEmployeeToTeam(User user, Team team) throws BaseException {
        Team teamFromRepo = teamRepository.findOne(team.getId());

        if (teamFromRepo == null) {
            throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST, "Could not add given user to team, since the team does not exist.");
        }
        teamFromRepo.addMember(user);
        teamRepository.save(teamFromRepo);
    }

}