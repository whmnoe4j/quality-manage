package com.xichuan.dev.jdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:00
 * @Description
 */
public interface PreparedStatementCreator {

    PreparedStatement createPreparedStatement(Connection conn) throws SQLException;
}