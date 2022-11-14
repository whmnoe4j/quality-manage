package com.xichuan.dev.source;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:31
 * @Description
 */
public interface AuditResultSetExtractor<E> {

    E extractData(ResultSet rs) throws SQLException;
}
