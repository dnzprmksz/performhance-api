package com.monitise.models;

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

    /**
     *  TEAM RELATED RESPONSE CODES
     */
    public static final int TEAM_ID_DOES_NOT_EXIST = 1301;
    public static final int TEAM_ID_INVALID = 1302;

    protected ResponseCode() {}
}