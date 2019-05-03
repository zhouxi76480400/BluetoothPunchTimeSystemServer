package org.xi.myserver.utils;

import java.io.Serializable;

public class SQLStatusCODEList implements Serializable {

    public static int DB_OK = 0;

    public static int DB_OTHER_EXCEPTION = -1;

    public static int DB_CANNOT_GET_CONNECTION = -2;

    public static int DB_CANNOT_CREATE_PREPARED_STATEMENT = -3;

    public static int DB_CANNOT_EXECUTE_SQL = -4;

    public static int DB_CANNOT_GET_RESULT_SET = -5;

    public static int OPT_UNIQUE_STATUS_EXIST = -1;

    public static int OPT_UNIQUE_STATUS_NOT_EXIST = 0;



}
