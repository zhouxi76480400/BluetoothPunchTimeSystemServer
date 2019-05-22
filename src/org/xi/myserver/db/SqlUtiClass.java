package org.xi.myserver.db;

import org.xi.myserver.pojo.DataTableObject;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.SQLStatusCODEList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sound.midi.SysexMessage;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqlUtiClass {


    public static DataSource getDataSource() {
        DataSource dataSource = null;
        try {
            Context context = new InitialContext();
            dataSource = (DataSource) context.lookup("java:comp/env/jdbc/punchtimesystem");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return dataSource;
    }

    public static SQLReturnDataClass getUserList(int l,int c) {
        // if l = 0
        // return first page
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createQueryUserDataSQL(c,l));
                if(preparedStatement != null) {
                    boolean isResultSet = executeSQL(sqlReturnDataClass,preparedStatement);
                    if(isResultSet) {
                        ResultSet resultSet = getResultSetFromSQL(sqlReturnDataClass,preparedStatement);
                        if(resultSet != null) {
                            List<StudentInformationObject> studentInformationObjectList = new ArrayList<>();
                            try {
                                while (resultSet.next()) {
                                    StudentInformationObject studentInformationObject = new StudentInformationObject();
                                    studentInformationObject.id = (long) resultSet.getInt("id");
                                    studentInformationObject.mac_address =
                                            resultSet.getString("mac_address");
                                    studentInformationObject.student_number =
                                            resultSet.getString("student_number");
                                    studentInformationObject.first_name = resultSet.getString("first_name");
                                    studentInformationObject.last_name = resultSet.getString("last_name");
                                    studentInformationObjectList.add(studentInformationObject);
                                }
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            sqlReturnDataClass.payload = studentInformationObjectList;
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass checkUQ(String table,String field,String checkData) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createCheckValueExistSQL(table,field));
                if(preparedStatement != null) {
                    try {
                        preparedStatement.setString(1,checkData);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    boolean isResultSet = executeSQL(sqlReturnDataClass,preparedStatement);
                    if(isResultSet) {
                        ResultSet resultSet = getResultSetFromSQL(sqlReturnDataClass,preparedStatement);
                        if(resultSet != null) {
                            int count = 0;
                            try {
                                resultSet.last();
                                count = resultSet.getRow();
                                resultSet.first();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            if(count > 0) {
                                sqlReturnDataClass.OPT_ERR_CODE = SQLStatusCODEList.OPT_UNIQUE_STATUS_EXIST;
                            }else {
                                sqlReturnDataClass.OPT_ERR_CODE = SQLStatusCODEList.OPT_UNIQUE_STATUS_NOT_EXIST;
                            }
                            try {
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass writeNewUserToDB(StudentInformationObject studentInformationObject) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        String mac = studentInformationObject.mac_address;
        String sn = studentInformationObject.student_number;
        String fn = studentInformationObject.first_name;
        String ln = studentInformationObject.last_name;
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createWriteNewUserSQL());
                if(preparedStatement != null) {
                    try {
                        preparedStatement.setString(1,mac);
                        preparedStatement.setString(2,sn);
                        preparedStatement.setString(3,fn);
                        preparedStatement.setString(4,ln);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    int update_count = 0;
                    try {
                        update_count = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
                        e.printStackTrace();
                    }
                    if(update_count > 0) {
                        //get data id
                        SQLReturnDataClass get_id_result = getIdWithMACandSN(mac,sn);
                        if(get_id_result.DB_ERR_CODE == SQLStatusCODEList.DB_OK) {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            sqlReturnDataClass.payload = get_id_result.payload;
                        }else {
                            sqlReturnDataClass.DB_ERR_CODE = get_id_result.DB_ERR_CODE;
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass getIdWithMACandSN(String mac,String student_number) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null){
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            PreparedStatement preparedStatement = getPreparedStatement(
                    sqlReturnDataClass,connection,CreateSqlStatementClass.queryIDWithMACAndSN());
            if(preparedStatement != null) {
                try {
                    preparedStatement.setString(1,mac);
                    preparedStatement.setString(2,student_number);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                boolean isResult = executeSQL(sqlReturnDataClass,preparedStatement);
                if(isResult) {
                    ResultSet resultSet = getResultSetFromSQL(sqlReturnDataClass,preparedStatement);
                    if(resultSet != null) {
                        int id = 0;
                        try {
                            while (resultSet.next()) {
                                id = resultSet.getInt("id");
                            }
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        if(id > 0) {
                            sqlReturnDataClass.payload = id;
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                        }else {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OTHER_EXCEPTION;
                        }
                        try {
                            resultSet.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass getCountWithId(int id) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createQueryUserIdCountSQL());
                if(preparedStatement != null) {
                    try {
                        preparedStatement.setInt(1,id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    boolean isResult = executeSQL(sqlReturnDataClass,preparedStatement);
                    if(isResult) {
                        ResultSet resultSet = getResultSetFromSQL(sqlReturnDataClass,preparedStatement);
                        if(resultSet != null) {
                            int count = 0;
                            try {
                                while (resultSet.next()) {
                                    count = resultSet.getInt("COUNT(*)");
                                }
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            boolean isExist = false;
                            if(count >= 1) {
                                isExist = true;
                            }else {
                                isExist = false;
                            }
                            sqlReturnDataClass.payload = isExist;
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            try {
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass deleteUserWithId(int id) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createQueryUserIdSQL());
                if(preparedStatement != null) {
                    try {
                        preparedStatement.setInt(1,id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    int update = 0;
                    try {
                        update = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    sqlReturnDataClass.payload = update;
                    sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    public static SQLReturnDataClass modifyUserToDB(StudentInformationObject studentInformationObject) {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OTHER_EXCEPTION;
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = getConnection(sqlReturnDataClass, dataSource);
            if(connection != null) {
                // check id is exists
                PreparedStatement preparedStatement = getPreparedStatement(sqlReturnDataClass,connection,
                        CreateSqlStatementClass.createQueryUserIdCountSQL());
                if(preparedStatement != null) {
                    try {
                        preparedStatement.setInt(1, (int) studentInformationObject.id);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    boolean isExecuted = executeSQL(sqlReturnDataClass,preparedStatement);
                    int count = 0;
                    if(isExecuted) {
                        ResultSet resultSet = getResultSetFromSQL(sqlReturnDataClass,preparedStatement);
                        if(resultSet != null) {
                            try {
                                while (resultSet.next()) {
                                    count = resultSet.getInt("COUNT(*)");
                                }
                                resultSet.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    if(count == 0) {
                        sqlReturnDataClass.OPT_ERR_CODE = SQLStatusCODEList.OPT_DB_ID_NOT_EXIST;
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                    }else {
                        // write to db
                        preparedStatement = getPreparedStatement(
                                sqlReturnDataClass,connection,CreateSqlStatementClass.createModifyUserWithIdSQL());
                        if(preparedStatement != null) {
                            try {
                                preparedStatement.setString(1,studentInformationObject.mac_address);
                                preparedStatement.setString(2,studentInformationObject.student_number);
                                preparedStatement.setString(3,studentInformationObject.last_name);
                                preparedStatement.setString(4,studentInformationObject.first_name);
                                preparedStatement.setInt(5, (int) studentInformationObject.id);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            int update_count = queryUpdateSQL(sqlReturnDataClass,preparedStatement);
                            try {
                                preparedStatement.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            if(update_count > 0) {
                                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            }else {
                                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OTHER_EXCEPTION;
                            }
                        }
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return sqlReturnDataClass;
    }

    private static ResultSet getResultSetFromSQL(SQLReturnDataClass sqlReturnDataClass,
                                                 PreparedStatement preparedStatement) {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.getResultSet();
        } catch (SQLException e) {
            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_RESULT_SET;
            e.printStackTrace();
        }
        return resultSet;
    }

    private static int queryUpdateSQL(SQLReturnDataClass sqlReturnDataClass, PreparedStatement preparedStatement) {
        int sql = -1;
        try {
            sql = preparedStatement.executeUpdate();
        } catch (SQLException e) {
            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
            e.printStackTrace();
        }
        return sql;
    }

    private static boolean executeSQL(SQLReturnDataClass sqlReturnDataClass, PreparedStatement preparedStatement) {
        boolean isExecuteSQL = false;
        try {
            isExecuteSQL = preparedStatement.execute();
        } catch (SQLException e) {
            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
            e.printStackTrace();
        }
        return isExecuteSQL;
    }

    private static PreparedStatement getPreparedStatement(SQLReturnDataClass sqlReturnDataClass,
                                                          Connection connection, String sql) {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
            e.printStackTrace();
        }
        return preparedStatement;
    }

    private static Connection getConnection(SQLReturnDataClass sqlReturnDataClass, DataSource dataSource) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
            e.printStackTrace();
        }
        return connection;
    }


    public static SQLReturnDataClass getDataTableList() {
        SQLReturnDataClass sqlReturnDataClass = new SQLReturnDataClass();
        DataSource dataSource = getDataSource();
        if(dataSource != null) {
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(connection != null) {
                PreparedStatement preparedStatement = getPreparedStatement(
                        sqlReturnDataClass,connection,CreateSqlStatementClass.createReadAllTablesSQL());
                if(preparedStatement != null) {
                    boolean isResultSet = executeSQL(sqlReturnDataClass,preparedStatement);
                    if(isResultSet) {
                        ResultSet resultSet = null;
                        try {
                            resultSet = preparedStatement.getResultSet();
                        } catch (SQLException e) {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_RESULT_SET;
                            e.printStackTrace();
                        }
                        if(resultSet != null) {
                            List<DataTableObject> punchList = new ArrayList<>();
                            try {
                                while (resultSet.next()) {
                                    DataTableObject databaseObject = new DataTableObject();
                                    String table_name = resultSet.getString("TABLE_NAME");
                                    databaseObject.table_name = table_name;
                                    Timestamp timestamp = resultSet.getTimestamp("CREATE_TIME");
                                    long time = timestamp.getTime();
                                    databaseObject.create_time = time;
                                    punchList.add(databaseObject);
                                }
                                resultSet.close();
                            }catch (SQLException e) {
                                e.printStackTrace();
                            }
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                            sqlReturnDataClass.payload = punchList;
                        }
                    }
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
            }
        }
        return sqlReturnDataClass;
    }

}
