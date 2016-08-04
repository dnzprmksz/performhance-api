package com.monitise.services;

import com.monitise.models.BaseException;
import com.monitise.models.User;
import com.monitise.models.ResponseCode;
import com.monitise.models.Team;
import com.monitise.repositories.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    public Team add(Team team) throws BaseException {

        // Add given team to repository.
        Team teamFromRepo = teamRepository.save(team);

        // Check the success of the action and throw an exception if the action fails.
        if (team == null) {
            throw new BaseException(ResponseCode.UNEXPECTED, "Could not add given team.");
        }

        return  teamFromRepo;
    }

    public void assingEmployeeToTeam(User user, Team team) {
        Team teamFromRepo = teamRepository.findOne(team.getId());
        teamFromRepo.addMember(user);
        teamRepository.save(teamFromRepo);
    }



}