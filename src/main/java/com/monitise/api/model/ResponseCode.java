package com.monitise.entity;

public class ResponseCode {

    /**
     * GENERAL RESPONSE CODES
     */
    public static final int UNEXPECTED = 1000;

    /**
     *  ORGANIZATION RELATED RESPONSE CODES
     */
    public static final int ORGANIZATION_ID_DOES_NOT_EXIST = 1101;
    public static final int ORGANIZATION_ID_INVALID = 1102;
    public static final int ORGANIZATION_NAME_DOES_NOT_EXIST = 1103;
    public static final int ORGANIZATION_NAME_INVALID = 1104;
    public static final int ORGANIZATION_NAME_EXISTS = 1105;

    /**
     *  USER RELATED RESPONSE CODES
     */
    public static final int USER_ID_DOES_NOT_EXIST = 1201;
    public static final int USER_ID_INVALID = 1202;
    public static final int USER_ROLE_INCORRECT = 1203;
    public static final int USER_USERNAME_NOT_EXIST = 1204;
    public static final int USER_UNAUTHORIZED_ORGANIZATION = 1205;

    /**
     *  TEAM RELATED RESPONSE CODES
     */
    public static final int TEAM_ID_DOES_NOT_EXIST = 1301;
    public static final int TEAM_ID_INVALID = 1302;

    /**
     *  JOB TITLE RELATED RESPONSE CODES
     */
    public static final int JOB_TITLE_ID_DOES_NOT_EXIST = 1401;
    public static final int JOB_TITLE_ID_INVALID = 1402;

    /**
     *  CRITERIA RELATED RESPONSE CODES
     */
    public static final int CRITERIA_ID_DOES_NOT_EXIST = 1501;
    public static final int CRITERIA_ID_INVALID = 1502;
    public static final int CRITERIA_EXISTS_IN_ORGANIZATION = 1503;
    public static final int CRITERIA_EMPTY = 1504;
    public static final int CRITERIA_EMPTY_ORGANIZATION = 1505;
    public static final int CRITERIA_EXISTS = 1506;

    protected ResponseCode() {}
}