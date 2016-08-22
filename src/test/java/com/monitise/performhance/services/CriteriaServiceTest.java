package com.monitise.performhance.services;

import com.monitise.performhance.AppConfig;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.matcher.CustomMatcher;
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
    @Autowired
    CriteriaService criteriaService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();


    @Test
    public void getAllFilterByOrganizationId() throws BaseException {
        List<Criteria> list = criteriaService.getAllFilterByOrganizationId(1);

        Assert.assertEquals(4, list.size());
        Assert.assertTrue(listContainsCriteria(list, 1, 1, "Manners"));
        Assert.assertTrue(listContainsCriteria(list, 2, 1, "Code Review"));
        Assert.assertTrue(listContainsCriteria(list, 3, 1, "Code Coverage"));
        Assert.assertTrue(listContainsCriteria(list, 3, 1, "Dress Code"));
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
        thrown.expect(CustomMatcher.hasCode(ResponseCode.SEARCH_INVALID_ID_FORMAT));
        throw new BaseException(ResponseCode.ORGANIZATION_ID_DOES_NOT_EXIST,"");
//        Criteria criteria = criteriaService.get(1080801);
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