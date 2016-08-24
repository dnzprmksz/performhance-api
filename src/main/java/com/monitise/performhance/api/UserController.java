package com.monitise.performhance.api;

import com.monitise.performhance.api.model.AddUserRequest;
import com.monitise.performhance.api.model.AverageCriteriaScore;
import com.monitise.performhance.api.model.CriteriaResponse;
import com.monitise.performhance.api.model.CriteriaUserResponse;
import com.monitise.performhance.api.model.EmployeeScoreResponse;
import com.monitise.performhance.api.model.Response;
import com.monitise.performhance.api.model.ResponseCode;
import com.monitise.performhance.api.model.SimplifiedUser;
import com.monitise.performhance.api.model.UpdateUserRequest;
import com.monitise.performhance.api.model.UserResponse;
import com.monitise.performhance.entity.Criteria;
import com.monitise.performhance.entity.JobTitle;
import com.monitise.performhance.entity.Organization;
import com.monitise.performhance.entity.Review;
import com.monitise.performhance.entity.User;
import com.monitise.performhance.exceptions.BaseException;
import com.monitise.performhance.helpers.RelationshipHelper;
import com.monitise.performhance.helpers.SecurityHelper;
import com.monitise.performhance.helpers.Util;
import com.monitise.performhance.services.CriteriaService;
import com.monitise.performhance.services.JobTitleService;
import com.monitise.performhance.services.OrganizationService;
import com.monitise.performhance.services.UserService;
import org.omg.CORBA.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    // region Dependencies

    @Autowired
    private SecurityHelper securityHelper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private JobTitleService jobTitleService;
    @Autowired
    private RelationshipHelper relationshipHelper;
    @Autowired
    private CriteriaService criteriaService;

    // endregion

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Response<List<SimplifiedUser>> getAll() throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        List<User> employees = userService.getByOrganizationId(organizationId);

        List<SimplifiedUser> simplifiedList = SimplifiedUser.fromUserList(employees);
        Response<List<SimplifiedUser>> response = new Response<>();
        response.setData(simplifiedList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public Response<SimplifiedUser> add(@RequestBody AddUserRequest addUserRequest) throws BaseException {
        int organizationId = securityHelper.getAuthenticatedUser().getOrganization().getId();
        Organization organization = organizationService.get(organizationId);
        validateUserRequest(organization, addUserRequest);
        JobTitle jobTitle = jobTitleService.get(addUserRequest.getJobTitleId());

        User employee = new User(
                addUserRequest.getName(),
                addUserRequest.getSurname(),
                organization,
                jobTitle,
                addUserRequest.getUsername(),
                addUserRequest.getPassword()
        );
        User addedEmployee = userService.addEmployee(employee);

        SimplifiedUser responseEmployee = SimplifiedUser.fromUser(addedEmployee);
        Response<SimplifiedUser> response = new Response<>();
        response.setData(responseEmployee);
        response.setSuccess(true);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public Response<SimplifiedUser> get(@PathVariable int userId) throws BaseException {
        checkAuthentication(userId);
        User user = userService.get(userId);

        SimplifiedUser responseUser = SimplifiedUser.fromUser(user);
        Response response = new Response();
        response.setData(responseUser);
        response.setSuccess(true);
        return response;
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public Response<Object> remove(@PathVariable int userId) throws BaseException {
        checkAuthentication(userId);
        userService.remove(userId);
        Response response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public Response<UserResponse> update(@PathVariable int userId,
                                         @RequestBody UpdateUserRequest updateUserRequest) throws BaseException {
        checkAuthentication(userId);
        User userFromService = userService.updateFromRequest(updateUserRequest, userId);

        UserResponse userResponse = UserResponse.fromUser(userFromService);
        Response<UserResponse> response = new Response<>();
        response.setData(userResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria", method = RequestMethod.GET)
    public Response<List<CriteriaResponse>> getUserCriteriaList(@PathVariable int userId) throws BaseException {
        checkAuthentication(userId);
        User user = userService.get(userId);
        List<Criteria> criteriaList = user.getCriteriaList();

        List<CriteriaResponse> criteriaResponseList = CriteriaResponse.fromList(criteriaList);
        Response<List<CriteriaResponse>> response = new Response<>();
        response.setData(criteriaResponseList);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria/{criteriaId}", method = RequestMethod.POST)
    public Response<CriteriaUserResponse> assignCriteriaToUser(@PathVariable int userId,
                                                               @PathVariable int criteriaId) throws BaseException {
        checkAuthentication(userId);
        securityHelper.checkAuthentication(criteriaService.get(criteriaId).getOrganization().getId());

        User userFromService = criteriaService.assignCriteriaToUserById(criteriaId, userId);
        CriteriaUserResponse criteriaUserResponse = CriteriaUserResponse.fromUser(userFromService);
        Response<CriteriaUserResponse> response = new Response<>();
        response.setData(criteriaUserResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/{userId}/criteria/{criteriaId}", method = RequestMethod.DELETE)
    public Response<Object> removeCriteriaFromUser(@PathVariable int userId,
                                                   @PathVariable int criteriaId) throws BaseException {
        checkAuthentication(userId);
        securityHelper.checkAuthentication(criteriaService.get(criteriaId).getOrganization().getId());

        criteriaService.removeCriteriaFromUserById(criteriaId, userId);
        Response<Object> response = new Response<>();
        response.setSuccess(true);
        return response;
    }

    @RequestMapping(value = "/{userId}/score", method = RequestMethod.GET)
    public Response<EmployeeScoreResponse> getEmployeeReviewScore(@PathVariable int userId) throws BaseException {
        checkAuthentication(userId);
        User employee = userService.get(userId);

        List<AverageCriteriaScore> criteriaScores = calculateCriteriaScores(employee);
        int reviewCount = employee.getReviews().size();
        List<String> comments = findAllComments(employee);

        EmployeeScoreResponse employeeScoreResponse = new EmployeeScoreResponse(
                employee.getName() + " " + employee.getSurname(),
                reviewCount,
                criteriaScores,
                comments
        );
        Response<EmployeeScoreResponse> response = new Response<>();
        response.setData(employeeScoreResponse);
        response.setSuccess(true);
        return response;
    }

    @Secured("ROLE_MANAGER")
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public Response<List<SimplifiedUser>> searchUsers(
            @RequestParam(value = "titleId", required = false, defaultValue = UserService.UNDEFINED) String titleId,
            @RequestParam(value = "teamId", required = false, defaultValue = UserService.UNDEFINED) String teamId,
            @RequestParam(value = "name", required = false, defaultValue = UserService.UNDEFINED) String name,
            @RequestParam(value = "surname", required = false, defaultValue = UserService.UNDEFINED) String surname)
            throws BaseException {

        if (UserService.UNDEFINED.equals(titleId) && UserService.UNDEFINED.equals(teamId)
                && UserService.UNDEFINED.equals(name) && UserService.UNDEFINED.equals(surname)) {
            throw new BaseException(ResponseCode.SEARCH_MISSING_PARAMETERS,
                    "At least one of titleId, teamId, name or surname parameters must be specified.");
        }

        User manager = securityHelper.getAuthenticatedUser();
        Organization organization = manager.getOrganization();
        int organizationId = organization.getId();
        formatValidateSearchRequest(titleId, teamId);
        semanticallyValidate(organization, titleId, teamId);
        List<User> userList = userService.searchUsers(organizationId, teamId, titleId, name, surname);

        List<SimplifiedUser> simpleList = SimplifiedUser.fromUserList(userList);
        Response response = new Response();
        response.setData(simpleList);
        response.setSuccess(true);

        return response;
    }

    // region Helper Methods

    private void checkAuthentication(int userId) throws BaseException {
        User user = userService.get(userId);
        Organization organization = user.getOrganization();
        int organizationId = organization.getId();
        securityHelper.checkAuthentication(organizationId);
    }

    private void validateUserRequest(Organization organization, AddUserRequest request) throws BaseException {
        if (Util.isNullOrEmpty(request.getName())) {
            throw new BaseException(ResponseCode.USER_NAME_INVALID, "Empty name is not allowed.");
        }

        if (Util.isNullOrEmpty(request.getSurname())) {
            throw new BaseException(ResponseCode.USER_SURNAME_INVALID, "Empty surname is not allowed.");
        }

        String username = request.getUsername();
        if (Util.isNullOrEmpty(username)) {
            throw new BaseException(ResponseCode.USERNAME_INVALID, "Empty username is not allowed.");
        }

        if (Util.isNullOrEmpty(request.getPassword())) {
            throw new BaseException(ResponseCode.PASSWORD_INVALID, "Empty password is not allowed.");
        }

        // This may mean the request body does not include jobTitleId field.
        if (request.getJobTitleId() == 0) {
            throw new BaseException(ResponseCode.JOB_TITLE_ID_INVALID, "Empty job title id is not allowed.");
        }

        relationshipHelper.ensureOrganizationJobTitleRelationship(organization.getId(), request.getJobTitleId());
    }

    private void formatValidateSearchRequest(String titleId, String teamId) throws BaseException {
        if ((!titleId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(titleId))) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID,
                    "titleId must be positive integers");
        }

        if ((!teamId.equals(UserService.UNDEFINED) && !isNonNegativeInteger(teamId))) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID,
                    "teamId must be positive integers");
        }

    }

    private void semanticallyValidate(Organization organization, String titleId, String teamId) throws BaseException {
        int organizationId = organization.getId();

        // Check if the title is defined in the organization.
        if (!titleId.equals(UserService.UNDEFINED)) {
            int intTitleId = Integer.parseInt(titleId);
            if (!organizationService.isJobTitleDefined(organizationId, intTitleId)) {
                throw new BaseException(ResponseCode.JOB_TITLE_ID_DOES_NOT_EXIST,
                        "Given job title id is not existent in the organization");
            }
        }

        // Check if the team is defined in the organization.
        if (!teamId.equals(UserService.UNDEFINED)) {
            int intTeamId = Integer.parseInt(teamId);
            if (!organizationService.isTeamIdDefined(organizationId, intTeamId)) {
                throw new BaseException(ResponseCode.TEAM_ID_DOES_NOT_EXIST,
                        "Given team's id is not existent in the organization");
            }
        }
    }

    private boolean isNonNegativeInteger(String str) throws BaseException {
        int candidate;
        try {
            candidate = Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            throw new BaseException(ResponseCode.SEARCH_INVALID_ID_FORMAT, "id's must be positive integers");
        }
        return candidate > 0;
    }

    private List<AverageCriteriaScore> calculateCriteriaScores(User employee) {
        AverageCriteriaScore[] averageCriteriaScores = calculateAverageCriteriaScores(employee);
        List<AverageCriteriaScore> criteriaScores = new ArrayList<>();
        for (int index = 0; index < averageCriteriaScores.length; index++) {
            criteriaScores.add(averageCriteriaScores[index]);
        }
        return criteriaScores;
    }

    private AverageCriteriaScore[] calculateAverageCriteriaScores(User employee) {
        AverageCriteriaScore[] averageCriteriaScores = calculateTotalCriteriaScores(employee);
        for (int index = 0; index < averageCriteriaScores.length; index++) {
            AverageCriteriaScore current = averageCriteriaScores[index];
            int averageScore = current.getTotalScore() / current.getReviewTimes();
            current.setAverageScore(averageScore);
        }
        return averageCriteriaScores;
    }

    private AverageCriteriaScore[] calculateTotalCriteriaScores(User employee) {
        List<Review> employeeReviews = employee.getReviews();
        List<Criteria> employeeCriteriaList = employee.getCriteriaList();
        int employeeCriteriaCount = employeeCriteriaList.size();

        AverageCriteriaScore[] totalCriteriaScores = new AverageCriteriaScore[employeeCriteriaCount];
        int index = 0;
        for (Criteria criteria : employeeCriteriaList) {
            totalCriteriaScores[index++] = new AverageCriteriaScore(criteria.getCriteria());
        }

        for (Review review : employeeReviews) {
            Map<Criteria, Integer> evaluation = review.getEvaluation();
            index = 0;
            for (Criteria criteria : employeeCriteriaList) {
                if (evaluation.containsKey(criteria)) {
                    int criteriaScore = evaluation.get(criteria);
                    AverageCriteriaScore current = totalCriteriaScores[index++];
                    current.setTotalScore(current.getTotalScore() + criteriaScore);
                    current.setReviewTimes(current.getReviewTimes() + 1);
                }
            }
        }
        return totalCriteriaScores;
    }

    private List<String> findAllComments(User employee) {
        List<String> comments = new ArrayList<>();
        List<Review> employeeReviews = employee.getReviews();
        for (Review review : employeeReviews) {
            String comment = review.getComment();
            if (comment != null) {
                comments.add(comment);
            }
        }
        return comments;
    }

    // endregion

}
