package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.CustomMatcher;
import com.monitise.performhance.repositories.OrganizationRepository;
import com.monitise.performhance.repositories.TeamRepository;
import com.monitise.performhance.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Rule;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppConfig.class)
@WebAppConfiguration
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:populate.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:cleanup.sql")
})
@Transactional
public class CriteriaServiceTest {

    private final String TEST_CRITERIA_NAME = "WOXWQZOE3BN9";
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;

    @Test
    public void getAllFilterByOrganizationId() throws BaseException {
        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(1);

        Assert.assertEquals(4, list.size());
        Assert.assertTrue(listContainsCriteria(list, 1, 1, "Manners"));
        Assert.assertTrue(listContainsCriteria(list, 2, 1, "Code Review"));
        Assert.assertTrue(listContainsCriteria(list, 3, 1, "Code Coverage"));
        Assert.assertTrue(listContainsCriteria(list, 4, 1, "Dress Code"));
    }

    @Test
    public void get_existingCriteria() throws BaseException {
        Criteria criteria = criteriaService.get(4);

        Assert.assertNotNull(criteria);
        Assert.assertEquals("Dress Code", criteria.getCriteria());
        Assert.assertEquals(1, criteria.getOrganization().getId());
    }

    @Test
    public void get_nonExistingCriteria() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.get(1080801);
    }

    @Test
    public void add() throws BaseException {
        Organization organization = organizationRepository.findOne(1);
        Criteria criteria = new Criteria("Testing", organization);
        final int TEST_CRITERIA_ID = criteriaService.add(criteria).getId();

        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(1);

        Assert.assertEquals(5, list.size());
        Assert.assertTrue(listContainsCriteria(list, TEST_CRITERIA_ID, 1, "Testing"));
    }

    @Test
    public void assignCriteriaToUserList_legitIdList_shouldAssign() throws BaseException {
        List<Integer> idList = new ArrayList();
        idList.add(2);
        idList.add(3);
        idList.add(4);
        final int TEST_CRITERIA_ID = addTestCriteria(1);
        criteriaService.assignCriteriaToUserList(TEST_CRITERIA_ID, idList);

        User pelin = userRepository.findOne(2);
        User faruk = userRepository.findOne(3);
        User pelya = userRepository.findOne(4);

        Assert.assertTrue(listContainsCriteria(pelin.getCriteriaList(), TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
        Assert.assertTrue(listContainsCriteria(faruk.getCriteriaList(), TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
        Assert.assertTrue(listContainsCriteria(pelya.getCriteriaList(), TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
    }

    @Test
    public void assignCriteriaToUserList_nonExistingCriteriaId_shouldNotAssign() throws BaseException {
        List<Integer> idList = new ArrayList();
        idList.add(2);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToUserList(5005, idList);
    }


    @Test
    public void assignCriteriaToUserList_nonExistingUserIds_shouldNotAssign() throws BaseException {
        List<Integer> idList = new ArrayList();
        idList.add(2);
        idList.add(200101002);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToUserList(3, idList);
    }

    @Test
    public void assignCriteriaToUserList_UsersAndCriteriaDifferentOrganizations_shouldNotAssign() throws BaseException {
        List<Integer> idList = new ArrayList();
        idList.add(6);
        idList.add(7);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_BELONGS_TO_ANOTHER_ORGANIZATION));
        criteriaService.assignCriteriaToUserList(3, idList);
    }

    @Test
    public void assignCriteriaToTeam() throws BaseException {
        final int TEST_CRITERIA_ID = addTestCriteria(1);
        Assert.assertEquals(8, TEST_CRITERIA_ID);
        criteriaService.assignCriteriaToTeam(TEST_CRITERIA_ID, 1);
        List<Criteria> pelinCriteriaList = userRepository.findOne(2).getCriteriaList();
        List<Criteria> farukCriteriaList = userRepository.findOne(3).getCriteriaList();
        List<Criteria> pelyaCriteriaList = userRepository.findOne(4).getCriteriaList();

        Assert.assertEquals(3, pelinCriteriaList.size());
        Assert.assertEquals(3, farukCriteriaList.size());
        Assert.assertEquals(5, pelyaCriteriaList.size());
        Assert.assertTrue(listContainsCriteria(pelinCriteriaList, TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
        Assert.assertTrue(listContainsCriteria(farukCriteriaList, TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
        Assert.assertTrue(listContainsCriteria(pelyaCriteriaList, TEST_CRITERIA_ID, 1, TEST_CRITERIA_NAME));
    }

    @Test
    public void assignCriteriaToTeam_nonExistingTeam_shouldNotAssign() throws BaseException {
        final int TEST_CRITERIA_ID = addTestCriteria(1);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.TEAM_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToTeam(TEST_CRITERIA_ID, 1023201);
    }

    @Test
    public void assignCriteriaToTeam_nonExistingCriteria_shouldNotAssign() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToTeam(1964691, 1);
    }

    @Test
    public void assignCriteriaToTeam_criteriaTeamDifferentOrganizations_shouldNotAssign() throws BaseException {
        final int TEST_CRITERIA_ID = addTestCriteria(2);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_AND_TEAM_BELONG_TO_DIFFERENT_ORGANIZATIONS));
        criteriaService.assignCriteriaToTeam(TEST_CRITERIA_ID, 1);
    }


    @Test
    public void assignCriteriaToUserById() throws BaseException {
        criteriaService.assignCriteriaToUserById(2, 2);
        List<Criteria> pelinCriteriaList = userRepository.findOne(2).getCriteriaList();

        Assert.assertNotNull(pelinCriteriaList);
        Assert.assertEquals(3, pelinCriteriaList.size());
        Assert.assertTrue(listContainsCriteria(pelinCriteriaList, 2, 1, "Code Review"));
    }

    @Test
    public void assignCriteriaToUserById_userHasCriteria_shouldNotAdd() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_EXISTS_IN_USER));
        criteriaService.assignCriteriaToUserById(4, 4);
    }

    @Test
    public void assignCriteriaToUserById_nonExistingCriteria_shouldNotAdd() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToUserById(23532, 2);
    }

    @Test
    public void assignCriteriaToUserById_nonExistingUser_shouldNotAdd() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        criteriaService.assignCriteriaToUserById(2, 9985899);
    }

    @Test
    public void assignCriteriaToUserById_criteriaUserDifferentOrganizations_shouldNotAdd() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_AND_USER_BELONG_TO_DIFFERENT_ORGANIZATIONS));
        criteriaService.assignCriteriaToUserById(2, 7);
    }

    @Test
    public void removeCriteriaFromUserById() throws BaseException {
        criteriaService.removeCriteriaFromUserById(3, 2);

        List<Criteria> pelinCriteriaList = userRepository.findOne(2).getCriteriaList();

        Assert.assertNotNull(pelinCriteriaList);
        Assert.assertEquals(1, pelinCriteriaList.size());
        Assert.assertFalse(listContainsCriteria(pelinCriteriaList, 3, 1, "Code Coverage"));
    }

    @Test
    public void removeCriteriaFromUserById_UserDoesNotHaveCriteria_shouldNotRemove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_DOES_NOT_EXIST_IN_USER));
        criteriaService.removeCriteriaFromUserById(2, 2);
    }

    @Test
    public void removeCriteriaFromUserById_nonExistingCriteria_shouldNotRemove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.removeCriteriaFromUserById(3356533, 2);
    }

    @Test
    public void removeCriteriaFromUserById_nonExistingUser_shouldNotRemove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.USER_ID_DOES_NOT_EXIST));
        criteriaService.removeCriteriaFromUserById(2, 3356533);
    }

    @Test
    public void removeCriteriaFromUserById_CriteriaUserDifferentOrganizations_shouldNotRemove()
            throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_AND_USER_BELONG_TO_DIFFERENT_ORGANIZATIONS));
        criteriaService.removeCriteriaFromUserById(2, 7);
    }

    @Test
    public void remove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_CAN_NOT_BE_DELETED));
        criteriaService.remove(2);
    }

    @Test
    public void remove_nonExistingCriteria_shouldNotRemove() throws BaseException {
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.remove(34544543);
    }

    @Test
    public void remove_criteriaNotAssignedToAnyUser() throws BaseException {
        final int TEST_CRITERIA_ID = addTestCriteria(1);
        criteriaService.remove(TEST_CRITERIA_ID);
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_ID_DOES_NOT_EXIST));
        criteriaService.get(TEST_CRITERIA_ID);
    }

    @Test
    public void update() throws BaseException {
        Criteria manners = criteriaService.get(1);
        manners.setCriteria("Shop Lifting Skills");
        criteriaService.update(manners);

        Criteria fetched = criteriaService.get(1);
        Assert.assertEquals("Shop Lifting Skills", fetched.getCriteria());
        Assert.assertEquals(1, fetched.getOrganization().getId());
    }

    @Test
    public void update_emptyName_shouldNotUpdate() throws BaseException {
        Criteria manners = criteriaService.get(1);
        manners.setCriteria("");
        thrown.expect(CustomMatcher.hasCode(ResponseCode.CRITERIA_EMPTY));
        criteriaService.update(manners);

    }

    // Returns added criteria's id.
    private int addTestCriteria(int organizationId) throws BaseException {
        Organization organization = organizationRepository.findOne(organizationId);
        Criteria criteria = new Criteria(TEST_CRITERIA_NAME, organization);
        Criteria added = criteriaService.add(criteria);
        return added.getId();
    }

    private boolean listContainsUser(List<User> list, int id, String name, String surname) {
        for (User user : list) {
            if (user.getId() == id && user.getName().equals(name) && user.getSurname().equals(surname)) {
                return true;
            }
        }
        return false;
    }

    private boolean listContainsCriteria(List<Criteria> list, int id, int organizationId, String name) {
        for (Criteria criteria : list) {
            if (criteria.getId() == id && criteria.getCriteria().equals(name) &&
                    criteria.getOrganization().getId() == organizationId) {
                return true;
            }
        }
        return false;
    }
}