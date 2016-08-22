package com.monitise.performhance.api.model;

public class ResponseCode {

    /**
     * GENERAL RESPONSE CODES.
     */
    public static final int UNEXPECTED = 1000;

    /**
     * ORGANIZATION RELATED RESPONSE CODES.
     */
    public static final int ORGANIZATION_ID_DOES_NOT_EXIST = 1101;
    public static final int ORGANIZATION_ID_INVALID = 1102;
    public static final int ORGANIZATION_NAME_DOES_NOT_EXIST = 1103;
    public static final int ORGANIZATION_NAME_INVALID = 1104;
    public static final int ORGANIZATION_NAME_EXISTS = 1105;

    /**
     * USER RELATED RESPONSE CODES.
     */
    public static final int USER_ID_DOES_NOT_EXIST = 1201;
    public static final int USER_ID_INVALID = 1202;
    public static final int USER_ROLE_INCORRECT = 1203;
    public static final int USER_USERNAME_NOT_EXIST = 1204;
    public static final int USER_USERNAME_ALREADY_TAKEN = 1205;
    public static final int USER_UNAUTHORIZED_ORGANIZATION = 1206;
    public static final int USER_BELONGS_TO_ANOTHER_ORGANIZATION = 1207;
    public static final int USER_DOES_NOT_BELONG_TO_TEAM = 1208;

    /**
     * TEAM RELATED RESPONSE CODES.
     */
    public static final int TEAM_ID_DOES_NOT_EXIST = 1301;
    public static final int TEAM_ID_INVALID = 1302;
    public static final int TEAM_BELONGS_TO_ANOTHER_ORGANIZATION = 1303;
    public static final int TEAM_HAS_NO_LEADER = 1304;
    public static final int USER_ALREADY_A_MEMBER = 1305;
    public static final int USER_IS_NOT_A_MEMBER = 1306;
    public static final int INVALID_TEAM_NAME = 1307;

    /**
     * JOB TITLE RELATED RESPONSE CODES.
     */
    public static final int JOB_TITLE_ID_DOES_NOT_EXIST = 1401;
    public static final int JOB_TITLE_ID_INVALID = 1402;
    public static final int JOB_TITLE_BELONGS_TO_ANOTHER_ORGANIZATION = 1403;
    public static final int JOB_TITLE_IS_IN_USE = 1404;

    /**
     * CRITERIA RELATED RESPONSE CODES.
     */
    public static final int CRITERIA_ID_DOES_NOT_EXIST = 1501;
    public static final int CRITERIA_ID_INVALID = 1502;
    public static final int CRITERIA_EXISTS_IN_ORGANIZATION = 1503;
    public static final int CRITERIA_EMPTY = 1504;
    public static final int CRITERIA_EMPTY_ORGANIZATION = 1505;
    public static final int CRITERIA_EXISTS = 1506;
    public static final int CRITERIA_EXISTS_IN_USER = 1507;
    public static final int CRITERIA_DOES_NOT_EXIST_IN_USER = 1508;
    public static final int CRITERIA_EXISTS_IN_SOME_USERS = 1509;
    public static final int CRITERIA_BELONGS_TO_ANOTHER_ORGANIZATION = 1510;
    public static final int CRITERIA_AND_USER_BELONG_TO_DIFFERENT_ORGANIZATIONS = 1511;
    public static final int CRITERIA_CAN_NOT_BE_DELETED = 1512;

    /**
     * REVIEW RELATED RESPONSE CODES.
     */
    public static final int REVIEW_ID_DOES_NOT_EXIST = 1601;
    public static final int REVIEW_ID_INVALID = 1602;
    public static final int REVIEW_EVALUATION_VALUE_INVALID = 1603;
    public static final int REVIEW_USER_CRITERIA_LIST_NOT_SATISFIED = 1604;
    public static final int REVIEW_USER_IN_DIFFERENT_TEAM = 1605;
    public static final int REVIEW_SAME_USER = 1606;

    /**
     * RELATIONSHIP RELATED RESPONSE CODES.
     */
    public static final int RELATIONSHIP_MANAGER_REVIEW_UNSATISFIED = 1701;
    public static final int RELATIONSHIP_TEAM_LEADER_REVIEW_UNSATISFIED = 1702;
    public static final int RELATIONSHIP_JOB_TITLE_CRITERIA_UNSATISFIED = 1703;
    public static final int TEAM_AND_USER_BELONG_TO_DIFFERENT_ORGANIZATIONS = 1704;
    public static final int CRITERIA_AND_TEAM_BELONG_TO_DIFFERENT_ORGANIZATIONS = 1705;

    /**
     * SEARCH RELATED RESPONSE CODES
     */
    public static final int SEARCH_INVALID_ID = 1801;
    public static final int SEARCH_INVALID_ID_FORMAT = 1802;
    public static final int SEARCH_MISSING_PARAMETERS = 1803;

    protected ResponseCode() {
    }
}