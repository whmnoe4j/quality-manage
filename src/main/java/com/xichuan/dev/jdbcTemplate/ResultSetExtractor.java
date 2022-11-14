package com.xichuan.dev.jdbcTemplate;

import com.xichuan.dev.jdbcTemplate.exception.DataAccessException;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:58
 * @Description
 */
public interface ResultSetExtractor<T> {

    T extractData(ResultSet rs) throws SQLException, DataAccessException;
}
