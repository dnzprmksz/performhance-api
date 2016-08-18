package com.monitise.performhance.helpers;

import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.Team;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.TeamService;
import com.monitise.performhance.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RelationshipHelper {

    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CriteriaService criteriaService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityHelper securityHelper;

    public void ensureOrganizationJobTitleRelationship(int organizationId, int jobTitleId) throws BaseException {
        if (!organizationService.isJobTitleDefined(organizationService.get(organizationId), jobTitleId)) {
            throw new BaseException(ResponseCode.JOB_TITLE_BELONGS_TO_ANOTHER_ORGANIZATION,
                    "Given job title does not belong to this organization.");
        }
    }

    public void ensureOrganizationCriteriaRelationship(int organizationId, int criteriaId) throws BaseException {
        Criteria criteria = criteriaService.get(criteriaId);
        int id = criteria.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.CRITERIA_BELONGS_TO_ANOTHER_ORGANIZATION,
                    "Given criteria does not belong to this organization.");
        }
    }

    public void ensureOrganizationTeamRelationship(int organizationId, int teamId) throws BaseException {
        Team team = teamService.get(teamId);
        int id = team.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.TEAM_BELONGS_TO_ANOTHER_ORGANIZATION,
                    "Given team does not belong to this organization.");
        }
    }

    public void ensureOrganizationUserRelationship(int organizationId, int userId) throws BaseException {
        User user = userService.get(userId);
        int id = user.getOrganization().getId();
        if (id != organizationId) {
            throw new BaseException(ResponseCode.USER_BELONGS_TO_ANOTHER_ORGANIZATION,
                    "Given user does not belong to this organization.");
        }
    }

    public void ensureOrganizationUserListRelationship(int organizationId, List<Integer> userIdList)
            throws BaseException {
        for (int userId : userIdList) {
            ensureOrganizationUserRelationship(organizationId, userId);
        }
    }

    public void ensureTeamEmployeeRelationShip(int teamId, int employeeId) throws BaseException {
        User employee = userService.get(employeeId);
        Team team = teamService.get(teamId);
        Team employeesTeam = employee.getTeam();
        if (employeesTeam == null || employeesTeam.getId() != team.getId()) {
            throw new BaseException(ResponseCode.USER_DOES_NOT_BELONG_TO_TEAM,
                    "Given user is not a member of this team.");
        }
    }

    public void ensureTeamEmployeeIndependence(int teamId, int employeeId) throws BaseException {
        User employee = userService.get(employeeId);
        Team team = teamService.get(teamId);
        Team employeesTeam = employee.getTeam();
        if (employeesTeam != null && employeesTeam.getId() == team.getId()) {
            throw new BaseException(ResponseCode.USER_DOES_NOT_BELONG_TO_TEAM,
                    "Given user is already a member of this team.");
        }
    }


    @Secured("ROLE_MANAGER")
    public void ensureManagerReviewRelationship(Review review) throws BaseException {
        User manager = securityHelper.getAuthenticatedUser();
        if (manager.getOrganization().getId() != review.getOrganization().getId()) {
            throw new BaseException(ResponseCode.RELATIONSHIP_MANAGER_REVIEW_UNSATISFIED,
                    "Given review and current manager are in different organizations.");
        }
    }

    @Secured("ROLE_TEAM_LEADER")
    public void ensureTeamLeaderReviewRelationship(Review review) throws BaseException {
        User leader = securityHelper.getAuthenticatedUser();
        if (leader.getTeam().getId() != review.getTeam().getId()) {
            throw new BaseException(ResponseCode.RELATIONSHIP_TEAM_LEADER_REVIEW_UNSATISFIED,
                    "Given review and current team leader are in different teams.");
        }
    }


    public void ensureEmployeeRelationship(User first, User second) throws BaseException {
        if (first.getTeam() != second.getTeam()) {
            throw new BaseException(ResponseCode.REVIEW_USER_IN_DIFFERENT_TEAM,
                    "Reviewed employee and reviewer are in different teams.");
        } else if (first.getId() == second.getId()) {
            throw new BaseException(ResponseCode.REVIEW_SAME_USER, "You cannot review yourself.");
        }
    }

}
