package com.monitise.performhance.services;


import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.Role;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import org.junit.Assert;
import org.junit.Test;
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
    private UserService userService;
    @Autowired
    private TeamService teamService;


    @Test
    public void getAllTeamsOfAnOrganization() throws BaseException {
        List<Team> teams = teamService.getListFilterByOrganizationId(1);

        Assert.assertNotNull(teams);
        Assert.assertEquals(2, teams.size());
        Assert.assertTrue(listContainsTeam(teams, 1, "TeamPelin", 1, 3));
        Assert.assertTrue(listContainsTeam(teams, 3, "GoogleLeaderless", 1, 0));
    }

    @Test
    public void getExistingTeamById() throws BaseException {
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
    public void getNonExistingTeamById() throws BaseException {
        Team team = teamService.get(1123);
    }

    @Test(expected = BaseException.class)
    public void deleteTeam() throws BaseException {
        teamService.deleteTeam(1);

        User pelin = userService.get(2);
        User faruk = userService.get(3);
        User pelya = userService.get(4);

        Assert.assertEquals(Role.EMPLOYEE, pelin.getRole());
        Assert.assertNull(pelin.getTeam());
        Assert.assertNull(faruk.getTeam());
        Assert.assertNull(pelya.getTeam());

        List<Team> teams = teamService.getListFilterByOrganizationId(1);
        Assert.assertEquals(1, teams.size());
        Team team = teamService.get(1);
    }

    @Test(expected = BaseException.class)
    public void deleteNonExistingTeam() throws BaseException {
        teamService.deleteTeam(5000);
    }

    @Test
    public void assignEmployee() throws BaseException {
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

        User fatih = userService.get(5);
        Assert.assertEquals(1, fatih.getTeam().getId());
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeThatIsAlreadyInTheTeam() throws BaseException {
        teamService.assignEmployeeToTeam(3, 1);
    }

    @Test(expected = BaseException.class)
    public void assignNonExistingEmployeeToATeam() throws BaseException {
        teamService.assignEmployeeToTeam(5005, 1);
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeToANonExistinTeam() throws BaseException {
        teamService.assignEmployeeToTeam(5, 9876);
    }

    @Test(expected = BaseException.class)
    public void assignEmployeeFromAnotherOrganization() throws BaseException {
        teamService.assignEmployeeToTeam(7, 1);
    }

    @Test// Team leader is a member in this case.
    public void assignTeamLeader() throws BaseException {
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

    @Test// Team leader is not a member here.
    public void assignTeamLeaderThatIsNotAMember() throws BaseException {
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

    @Test
    public void assignNonExistingUserAsLeader() throws BaseException {
        teamService.assignLeaderToTeam(10000, 1);
    }

    @Test
    public void assignLeaderToNonExistinTeam() throws BaseException {
        teamService.assignLeaderToTeam(3, 10000);
    }

    @Test
    public void assignLeaderFromAnotherOrganization() throws BaseException {
        teamService.assignLeaderToTeam(7, 1);
    }

    @Test
    public void removeLeadershipFromTeam() throws BaseException {
        teamService.removeLeadershipFromTeam(1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User leader = team.getLeader();

        Assert.assertNull(leader);
        Assert.assertEquals(3, members.size());

        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 3, "Faruk", "Gulmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
        Assert.assertTrue(listContainsUser(members, 5, "Fatih", "Songul"));
    }

    @Test
    public void removeLeadershipFromATeamThatHasNoLeader() throws BaseException {
        teamService.removeLeadershipFromTeam(3);
    }

    @Test
    public void removeLeadershipFromNonExistingTeam() throws BaseException {
        teamService.removeLeadershipFromTeam(181818);
    }

    @Test
    public void removeReaderFromTeam() throws BaseException {
        teamService.removeEmployeeFromTeam(2, 1);

        User pelin = userService.get(2);
        Assert.assertEquals(Role.EMPLOYEE, pelin.getRole());
        Assert.assertNull(pelin.getTeam());

        Team pelinsOldTeam = teamService.get(1);
        Assert.assertNull(pelinsOldTeam.getLeader());
        Assert.assertEquals(2, pelinsOldTeam.getMembers().size());

    }

    @Test
    public void removeEmployeeFromTeam() throws BaseException {
        teamService.removeEmployeeFromTeam(3, 1);

        Team team = teamService.get(1);
        List<User> members = team.getMembers();
        User faruk = userService.get(3);

        Assert.assertNull(faruk.getTeam());
        Assert.assertEquals(2, members.size());
        Assert.assertTrue(listContainsUser(members, 2, "Pelin", "Sonmez"));
        Assert.assertTrue(listContainsUser(members, 4, "Pelya", "Petroffski"));
    }

    @Test
    public void removeNonExistingEmployeeFromTeam() throws BaseException {
        teamService.removeEmployeeFromTeam(10001, 1);
    }

    @Test
    public void removeEmployeeFromNonExistingTeam() throws BaseException {
        teamService.removeEmployeeFromTeam(2, 10001);
    }

    @Test
    public void removeEmployeeOfAnotherOrganizationFromTeam() throws BaseException {
        teamService.removeEmployeeFromTeam(7, 1);

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