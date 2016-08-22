package com.monitise.performhance.services;


import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
@Transactional
public class TeamServiceTest {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserRepository userRepository;


    @Test
    public void getListFilterByOrganizationId_allTeamsOfAnOrganization() throws BaseException {
        List<Team> teams = teamService.getListFilterByOrganizationId(1);

        Assert.assertNotNull(teams);
        Assert.assertEquals(2, teams.size());
        Assert.assertTrue(listContainsTeam(teams, 1, "TeamPelin", 1, 3));
        Assert.assertTrue(listContainsTeam(teams, 3, "GoogleLeaderless", 1, 0));
    }

    @Test
    public void get_existingTeamById() throws BaseException {
        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User leader = team.getLeader();

        Assert.assertNotNull(team);
        Assert.assertNotNull(members);
        Assert.assertNotNull(leader);

        Assert.assertEquals(1, team.getOrganization().getId());
        Assert.assertEquals(3, members.size());
        Assert.assertEquals("TeamPelin", team.getName());
        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
        Assert.assertEquals(2, leader.getId());
    }

    @Test(expected = BaseException.class)
    public void get_nonExistingTeamById() throws BaseException {
        Team team = teamService.get(1123);
    }

    @Test(expected = BaseException.class)
    public void deleteTeam_shouldDelete() throws BaseException {
        teamService.deleteTeam(1);

        User pelin = userRepository.findOne(2);
        User faruk = userRepository.findOne(3);
        User pelya = userRepository.findOne(4);

        Assert.assertEquals(Role.EMPLOYEE, pelin.getRole());
        Assert.assertNull(pelin.getTeam());
        Assert.assertNull(faruk.getTeam());
        Assert.assertNull(pelya.getTeam());

        List<Team> teams = teamService.getListFilterByOrganizationId(1);
        Assert.assertEquals(1, teams.size());
        Team team = teamService.get(1);
    }

    @Test(expected = BaseException.class)
    public void deleteTeam_nonExistingTeam_shouldNotDelete() throws BaseException {
        teamService.deleteTeam(5000);
    }

    @Test
    public void assignEmployeeToTeam_shouldAssign() throws BaseException {
        teamService.assignEmployeeToTeam(5, 1);
        Team team = teamService.get(1);
        List<User> members = team.getMembers();

        Assert.assertNotNull(team);
        Assert.assertNotNull(members);

        Assert.assertEquals(4, members.size());
        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
        Assert.assertTrue(listContainsUser(members, 5, "Fatih", "Songul"));

        User fatih = userRepository.findOne(5);
        Assert.assertEquals(1, fatih.getTeam().getId());
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeToTeam_employeeAlreadyInTheTeam_shouldNotAssign() throws BaseException {
        teamService.assignEmployeeToTeam(3, 1);
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeToTeam_nonExistingEmployee_shouldNotAssign() throws BaseException {
        teamService.assignEmployeeToTeam(5005, 1);
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeToTeam_nonExistingTeam_shouldNotAssign() throws BaseException {
        teamService.assignEmployeeToTeam(5, 9876);
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeToTeam_assignEmployeeFromAnotherOrganization() throws BaseException {
        teamService.assignEmployeeToTeam(7, 1);
    }

    @Test// Team leader is a member in this case.
    public void assignLeaderToTeam_shouldAssign() throws BaseException {
        teamService.assignLeaderToTeam(3, 1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User leader = team.getLeader();

        Assert.assertEquals(3, members.size());
        Assert.assertEquals(3, leader.getId());
        Assert.assertEquals("faruk.gulmez", leader.getUsername());

        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
    }

    @Test// Team leader is not a member here. Should add the leader to team --> assign as leader.
    public void assignLeaderToTeam_teamLeaderNotAMember_shouldAssign() throws BaseException {
        teamService.assignLeaderToTeam(5, 1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User leader = team.getLeader();

        Assert.assertEquals(4, members.size());
        Assert.assertEquals(5, leader.getId());
        Assert.assertEquals("fatih.songul", leader.getUsername());

        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
        Assert.assertTrue(listContainsUser(members, 5, "Fatih", "Songul"));

    }

    @Test(expected = BaseException.class)
    public void assignLeaderToTeam_nonExistingUser_shouldNotAssign() throws BaseException {
        teamService.assignLeaderToTeam(10000, 1);
    }

    @Test(expected = BaseException.class)
    public void assignLeaderToTeam_nonExistingTeam_shouldNotAssign() throws BaseException {
        teamService.assignLeaderToTeam(3, 10000);
    }

    @Test(expected = BaseException.class)
    public void assignLeaderToTeam_leaderFromOtherOrganization_shouldNotAssign() throws BaseException {
        teamService.assignLeaderToTeam(7, 1);
    }

    @Test
    public void removeLeadershipFromTeam_shouldRemove() throws BaseException {
        teamService.removeLeadershipFromTeam(1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User leader = team.getLeader();

        Assert.assertNull(leader);
        Assert.assertEquals(3, members.size());

        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
    }

    @Test(expected = BaseException.class)
    public void removeLeadershipFromTeam_teamHasNoLeader_shouldNotRemove() throws BaseException {
        teamService.removeLeadershipFromTeam(3);
    }

    @Test(expected = BaseException.class)
    public void removeLeadershipFromTeam_nonExistingTeam_shouldNotRemove() throws BaseException {
        teamService.removeLeadershipFromTeam(181818);
    }

    @Test
    public void removeEmployeeFromTeam_employeeIsTeamLeader_shouldRemove() throws BaseException {
        teamService.removeEmployeeFromTeam(2, 1);

        User pelin = userRepository.findOne(2);
        Assert.assertEquals(Role.EMPLOYEE, pelin.getRole());
        Assert.assertNull(pelin.getTeam());

        Team pelinsOldTeam = teamService.get(1);
        Assert.assertNull(pelinsOldTeam.getLeader());
        Assert.assertEquals(2, pelinsOldTeam.getMembers().size());

    }

    @Test
    public void removeEmployeeFromTeam_shouldRemove() throws BaseException {
        teamService.removeEmployeeFromTeam(3, 1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User faruk = userRepository.findOne(3);

        Assert.assertNull(faruk.getTeam());
        Assert.assertEquals(2, members.size());
        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
    }

    @Test(expected = BaseException.class)
    public void removeEmployeeFromTeam_nonExistingEmployee_shouldNotRemove() throws BaseException {
        teamService.removeEmployeeFromTeam(10001, 1);
    }

    @Test(expected = BaseException.class)
    public void removeEmployeeFromTeam_nonExistingTeam_shouldNotRemove() throws BaseException {
        teamService.removeEmployeeFromTeam(2, 10001);
    }

    @Test(expected = BaseException.class)
    public void removeEmployeeFromTeam_employeeFromOtherOrganization_shouldNotRemove() throws BaseException {
        teamService.removeEmployeeFromTeam(7, 1);
    }

    @Test
    public void searchTeams_undefTeamName() {
        String undef = TeamService.UNDEFINED;
        List<Team> teams = teamService.searchTeams(1, undef);

        Assert.assertEquals(2, teams.size());
        Assert.assertTrue(listContainsTeam(teams, 1, "TeamPelin", 1, 3));
        Assert.assertTrue(listContainsTeam(teams, 3, "GoogleLeaderless", 1, 0));
    }

    @Test
    public void searchTeams_partialTeamName_1() {
        List<Team> teams = teamService.searchTeams(1, "ea");

        Assert.assertEquals(2, teams.size());
        Assert.assertTrue(listContainsTeam(teams, 1, "TeamPelin", 1, 3));
        Assert.assertTrue(listContainsTeam(teams, 3, "GoogleLeaderless", 1, 0));
        ExpectedException.none();
    }

    @Test
    public void searchTeams_partialTeamName_2() {
        List<Team> teams = teamService.searchTeams(1, "team");

        Assert.assertEquals(1, teams.size());
        Assert.assertTrue(listContainsTeam(teams, 1, "TeamPelin", 1, 3));
    }

    private boolean listContainsUser(List<User> list, int id, String name, String surname) {
        for (User user : list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }

    private boolean listContainsTeam(List<Team> list, int id, String name, int organizationId, int size) {
        for (Team team : list) {
            if (team.getId() == id && team.getName().equals(name)
                    && team.getOrganization().getId() == organizationId
                    && team.getMembers().size() == size) {
                return true;
            }
        }
        return false;
    }

}