package com.xichuan.dev.sink;

import com.xichuan.dev.entity.JDBCEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/13 14:16
 * @Description
 */

/**
 * 抽象JDBC sink
 */
public abstract class JdbcSink {
    protected Connection connection;

    /**
     * JDBC init
     * @param jdbcEntity
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public abstract JdbcSink open(JDBCEntity jdbcEntity) throws SQLException, ClassNotFoundException;

    /**
     * Write sql to db
     * @param sql
     * @return
     * @throws Exception
     */
    protected boolean writeToDB(String sql)throws Exception{
        PreparedStatement ps  = connection.prepareStatement(sql);
        Boolean result = ps.execute();
        ps.close();

        return result;
    }

    /**
     * JDBC close
     */
    public abstract void close();

}
