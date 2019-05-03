package org.xi.myserver.db;

import java.awt.*;

public class CreateSqlStatementClass {

    private static String db_name = "punchtimesystem";

    public static final String user_list = "user_list";

    private static String read_sal_tables =
            "select * from information_schema.tables where table_schema = '%s';";

    public static String createReadAllTablesSQL() {
        return String.format(read_sal_tables,db_name);
    }

    private static String write_new_user =
            "INSERT INTO `%s`.`user_list` (`mac_address`, `student_number`, `last_name`, `first_name`" +
                    ") VALUES (?, ?, ?, ?);";

    public static String createWriteNewUserSQL() {
        String sql = String.format(write_new_user,db_name);
        return sql;
    }

    private static String check_xxx_exist = "SELECT * FROM %s.%s WHERE %s = ?;";

    public static String createCheckValueExistSQL(String table,String field) {
        String sql = String.format(check_xxx_exist,db_name,table,field);
        return sql;
    }

    private static String query_id_with_mac_and_sn =
            "SELECT * FROM %s.%s WHERE mac_address = ? and student_number = ?;";

    public static String queryIDWithMACAndSN() {
        String sql = String.format(query_id_with_mac_and_sn,db_name,user_list);
        return sql;
    }

    private static String query_user_data_sql1 =
            "SELECT * FROM %s.%s ORDER BY id DESC LIMIT %s;";

    private static String query_user_data_sql2 =
            "SELECT * FROM %s.%s WHERE id >= %s AND id < %s ORDER BY id DESC;";

    public static String createQueryUserDataSQL(int c, int l) {
        String sql = null;
        if(l == 0) {
            sql = String.format(query_user_data_sql1,db_name,user_list,String.valueOf(c));
        }else {
            int max = l;
            int min = l - c;
            sql = String.format(query_user_data_sql2,db_name,user_list,min,max);
        }
        return sql;
    }

}
