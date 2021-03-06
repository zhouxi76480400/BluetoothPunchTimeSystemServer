package org.xi.myserver.utils;

import java.io.Serializable;

public class StatusCodeList implements Serializable {

    public static final int STATUS_CODE_OK = 0;

    public static final int STATUS_CODE_PARAMETER_NOT_EQUALS = 1;

    public static final int STATUS_CODE_JSON_CONVERT_FAILED = 2;

    public static final int STATUS_CODE_JSON_PARAMETER_NOT_EQUALS = 3;

    public static final int STATUS_CODE_USER_DATA_NOT_WRITE_SUCCESS = 4;

    public static final int STATUS_CODE_MAC_ADDRESS_EXIST = 5;

    public static final int STATUS_CODE_STUDENT_NUMBER_EXIST = 6;

    public static final int STATUS_CODE_SQL_ID_NOT_EXIST = 7;

    public static final int STATUS_CODE_SQL_REMOVE_USER_FAILED = 8;

    public static final int STATUS_CODE_SESSION_ID_NOT_EXISTS = 9;

    public static final int STATUS_CODE_UPDATE_SESSION_DATA_FAILED = 10;

}
