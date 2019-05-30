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

    private static String query_user_id_exists =
            "SELECT COUNT(*) FROM %s.%s WHERE id = ?;";

    public static String createQueryUserIdCountSQL() {
        return String.format(query_user_id_exists,db_name,user_list);
    }

    private static String delete_user_with_id =
            "DELETE FROM %s.%s WHERE (`id` = ?);";

    public static String createQueryUserIdSQL() {
        return String.format(delete_user_with_id,db_name,user_list);
    }

    private static String modify_user_with_id =
            "UPDATE `%s`.`%s` SET `mac_address` = ?, `student_number` = ?, `last_name` = ?, `first_name` = ? WHERE (`id` = ?);";


    public static String createModifyUserWithIdSQL() {
        return String.format(modify_user_with_id,db_name,user_list);
    }

    public static String createDeleteUserWithIdSQL(int count) {
        String all_str = String.format(delete_user_with_id,db_name,user_list);
        return all_str;
    }

    private static String query_users_with_mac =
            "SELECT * FROM %s.%s WHERE mac_address IN (%s)";

    public static String createQueryUsersWithMACSQL(int users_count) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < users_count ; i ++) {
            stringBuilder.append("?");
            if(i != users_count - 1) {
                stringBuilder.append(", ");
            }
        }
        String sql = String.format(query_users_with_mac,db_name,user_list,stringBuilder.toString());
        System.out.println(sql);

        return sql;
    }


}
