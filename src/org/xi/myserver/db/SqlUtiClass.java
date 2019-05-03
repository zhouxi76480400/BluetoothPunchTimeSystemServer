package org.xi.myserver.db;

import org.xi.myserver.pojo.DataTableObject;
import org.xi.myserver.pojo.SQLReturnDataClass;
import org.xi.myserver.pojo.StudentInformationObject;
import org.xi.myserver.utils.SQLStatusCODEList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
                e.printStackTrace();
            }
            if(connection != null) {
                PreparedStatement preparedStatement = null;

                try {
                    preparedStatement =
                            connection.prepareStatement(CreateSqlStatementClass.createQueryUserDataSQL(c,l));
                } catch (SQLException e) {
                    sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
                    e.printStackTrace();
                }
                if(preparedStatement != null) {
                    boolean isResultSet = false;
                    try {
                        isResultSet = preparedStatement.execute();
                    } catch (SQLException e) {
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
                        e.printStackTrace();
                    }
                    if(isResultSet) {
                        ResultSet resultSet = null;
                        try {
                            resultSet = preparedStatement.getResultSet();
                        } catch (SQLException e) {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_RESULT_SET;
                            e.printStackTrace();
                        }
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
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
                e.printStackTrace();
            }
            if(connection != null) {
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = connection.
                            prepareStatement(CreateSqlStatementClass.createCheckValueExistSQL(table,field));
                    preparedStatement.setString(1,checkData);
                } catch (SQLException e) {
                    sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
                    e.printStackTrace();
                }
                if(preparedStatement != null) {
                    boolean isResultSet = false;
                    try {
                        isResultSet = preparedStatement.execute();
                    } catch (SQLException e) {
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
                        e.printStackTrace();
                    }
                    if(isResultSet) {
                        ResultSet resultSet = null;
                        try {
                            resultSet = preparedStatement.getResultSet();
                        } catch (SQLException e) {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_RESULT_SET;
                            e.printStackTrace();
                        }
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
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
                e.printStackTrace();
            }
            if(connection != null) {
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement =
                            connection.prepareStatement(CreateSqlStatementClass.createWriteNewUserSQL());
                    preparedStatement.setString(1,mac);
                    preparedStatement.setString(2,sn);
                    preparedStatement.setString(3,fn);
                    preparedStatement.setString(4,ln);
                } catch (SQLException e) {
                    sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
                    e.printStackTrace();
                }
                if(preparedStatement != null) {
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
            Connection connection = null;
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_CONNECTION;
                e.printStackTrace();
            }
            PreparedStatement preparedStatement = null;
            try {
                preparedStatement = connection.
                        prepareStatement(CreateSqlStatementClass.queryIDWithMACAndSN());
                preparedStatement.setString(1,mac);
                preparedStatement.setString(2,student_number);
            } catch (SQLException e) {
                sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
                e.printStackTrace();
            }
            if(preparedStatement != null) {
                boolean isResult = false;
                try {
                    isResult = preparedStatement.execute();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                if(isResult) {
                    ResultSet resultSet = null;
                    try {
                        resultSet = preparedStatement.getResultSet();
                    } catch (SQLException e) {
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_GET_RESULT_SET;
                        e.printStackTrace();
                    }
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
                            sqlReturnDataClass.payload = new Integer(id);
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OK;
                        }else {
                            sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_OTHER_EXCEPTION;
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
                PreparedStatement preparedStatement = null;
                try {
                    preparedStatement = connection.prepareStatement(CreateSqlStatementClass.createReadAllTablesSQL());
                } catch (SQLException e) {
                    sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_CREATE_PREPARED_STATEMENT;
                    e.printStackTrace();
                }
                if(preparedStatement != null) {
                    boolean isResultSet = false;
                    try {
                        isResultSet = preparedStatement.execute();
                    } catch (SQLException e) {
                        sqlReturnDataClass.DB_ERR_CODE = SQLStatusCODEList.DB_CANNOT_EXECUTE_SQL;
                        e.printStackTrace();
                    }
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
