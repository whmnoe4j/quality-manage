package com.xichuan.dev.util;

import java.sql.*;

/**
 * @Author Xichuan
 * @Date 2022/4/14 14:49
 * @Description
 */
public class JDBCUtil {

    public static Connection getConnection(String driver,String url,String user,String password) throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        return DriverManager.getConnection(url,user,password);
    }

    public static void close(ResultSet rs, PreparedStatement stmt, Connection conn) throws SQLException {
        if (rs != null) {
            rs.close();
        }
        if (stmt != null) {
            stmt.close();
        }
        if (conn != null) {
            conn.close();
        }
    }

}
