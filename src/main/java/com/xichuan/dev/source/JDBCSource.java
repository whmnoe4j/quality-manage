package com.xichuan.dev.source;

import com.xichuan.dev.entity.JDBCEntity;
import org.slf4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/13 18:42
 * @Description
 */
public abstract class JDBCSource implements Source<ResultSet,String> {
    protected Logger logger;

    protected Connection connection;
    protected ResultSet rs;

    /**
     * JDBC init
     * @param jdbcEntity
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public abstract JDBCSource open(JDBCEntity jdbcEntity) throws SQLException, ClassNotFoundException;

    /**
     * executeSql
     * @param sql
     * @return
     * @throws Exception
     */
    protected ResultSet executeSql(String sql)throws Exception{
        rs = connection.prepareStatement(sql).executeQuery();
        return rs;
    }

    /**
     * read
     * @param sql
     * @return
     * @throws Exception
     */
    @Override
    public ResultSet read(String sql)throws Exception {
        return executeSql(sql);
    }

    /**
     * read data
     * @param sql
     * @param extractor
     * @param <E>
     * @return
     */
    public <E> E read(String sql, AuditResultSetExtractor<E> extractor){

        E t = null;
        try {
            if (connection == null){
                logger.error("connection is not initialized！");
                return null;
            }

            logger.info("sql:"+sql);
            rs = read(sql);
            t = extractor.extractData(rs);
        } catch (Exception e) {
            logger.error("Failed to obtain data from the db！," + e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    /**
     * JDBC close
     */
    public abstract void close();
}
