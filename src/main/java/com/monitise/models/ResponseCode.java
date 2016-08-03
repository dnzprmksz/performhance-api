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

    protected ResponseCode() {}
}