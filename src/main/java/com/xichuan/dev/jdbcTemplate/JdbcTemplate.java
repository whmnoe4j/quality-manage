package com.xichuan.dev.jdbcTemplate;


import com.xichuan.dev.jdbcTemplate.exception.DataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Xichuan
 * @Date 2022/4/15 10:02
 * @Description
 */
public class JdbcTemplate {
    private DataSource dataSource;
    private Connection connection;
    private Statement st;
    private PreparedStatement pr;
    private ResultSet rs;

    public JdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public JdbcTemplate() {
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void closeSource() {
        try {
            if (rs != null) rs.close();
            if (pr != null) pr.close();
            if (st != null) st.close();
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getDefaultPreparedStatement(String sql, Object[] args, int[] argTypes) throws SQLException {
        connection = dataSource.getConnection();
        DefaultPreparedStatementSetter setter = new DefaultPreparedStatementSetter(args, argTypes);
        pr = new DefaultPreparedStatementCreator(setter, sql).createPreparedStatement(connection);
    }

    private int executeUpdate(String sql, Object[] args, int[] argTypes) {
        int i = 0;
        try {
            getDefaultPreparedStatement(sql, args, argTypes);
            i = pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    private ResultSet executeQuery(String sql, Object[] args, int[] argTypes) throws SQLException {
        getDefaultPreparedStatement(sql, args, argTypes);
        return pr.executeQuery();
    }


    public int update(final String sql) {
        return this.executeUpdate(sql, null, null);
    }

    public int update(PreparedStatementCreator psc) {
        int i = 0;
        try {
            pr = psc.createPreparedStatement(dataSource.getConnection());
            i = pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return i;
    }

    public int update(PreparedStatementCreator psc, final keyHolder generatedKeyHolder) {
        int i = 0;
        try {
            pr = psc.createPreparedStatement(dataSource.getConnection());
            i = pr.executeUpdate();
            rs = pr.getGeneratedKeys();
            int id = 0;
            if (rs.next()) {
                id = rs.getInt(1);
            }
            generatedKeyHolder.setKey(id);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return i;
    }

    public int update(String sql, PreparedStatementSetter setter) {
        int i = 0;
        try {
            connection = dataSource.getConnection();
            pr = connection.prepareStatement(sql);
            setter.setValues(pr);
            i = pr.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return i;
    }

    public int update(String sql, Object[] args, int[] argTypes) {
        return this.executeUpdate(sql, args, argTypes);
    }

    public int update(String sql, Object... args) {
        return this.update(sql, args, null);
    }

    public int[] batchUpdate(final String[] sql) {
        int[] results = null;
        try {
            connection = dataSource.getConnection();
            st = connection.createStatement();
            for (String sqlEle : sql) {
                st.addBatch(sqlEle);
            }
            results = st.executeBatch();
            st.clearBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return results;
    }

    public int[] batchUpdate(String sql, final BatchPreparedStatementSetter pss) {
        int[] results = null;
        try {
            connection = dataSource.getConnection();
            pr = connection.prepareStatement(sql);
            int batchSize = pss.getBatchSize();
            for (int i = 0; i < batchSize; i++) {
                pss.setValues(pr, i + 1);
                pr.addBatch();
            }
            results = pr.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs) {
        return this.batchUpdate(sql, batchArgs, null);
    }

    public int[] batchUpdate(String sql, List<Object[]> batchArgs, int[] argTypes) {
        return this.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int index) throws SQLException {
                Object[] args = batchArgs.get(index);
                DefaultPreparedStatementSetter setter = new DefaultPreparedStatementSetter(args, argTypes);
                setter.setValues(pr);
            }

            @Override
            public int getBatchSize() {
                return batchArgs.size();
            }
        });
    }

    public <T> T queryForObject(String sql, Class<T> requiredType) {
        return this.queryForObject(sql, null, requiredType);
    }

    public <T> T queryForObject(String sql, Object[] args, Class<T> requiredType) {
        return this.query(sql, args, new ResultSetExtractor<T>() {
            @Override
            public T extractData(ResultSet rs) throws SQLException, DataAccessException {
                T result = null;
                if (rs.next()) {
                    result = (T) rs.getObject(1);
                }
                return result;
            }
        });
    }


    public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
        return this.queryForObject(sql, null, rowMapper);
    }

    public <T> T queryForObject(String sql, Object[] args, RowMapper<T> rowMapper) {
        return this.queryForObject(sql, args, null, rowMapper);
    }

    public <T> T queryForObject(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
        List<T> result = this.query(sql, args, argTypes, rowMapper);
        if (result == null || result.size() == 0) {
            throw new RuntimeException("没有结果");
        }
        return result.get(0);
    }

    public <T> T queryForObject(String sql, RowMapper<T> rowMapper, Object... args) {
        return this.queryForObject(sql, args, rowMapper);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return this.queryForList(sql, null, null, null);
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType) {
        return this.queryForList(sql, null, elementType);
    }

    public <T> List<T> queryForList(String sql, Object[] args, Class<T> elementType) {
        return this.queryForList(sql, args, null, elementType);
    }

    public <T> List<T> queryForList(String sql, Object[] args, int[] argTypes, Class<T> elementType) {
        return this.query(sql, args, argTypes, new RowMapper<T>() {
            @Override
            public <T> T mapRow(ResultSet rs, int rowNum) throws SQLException {
                return (T) rs.getObject(1);
            }
        });
    }

    public <T> List<T> queryForList(String sql, Class<T> elementType, Object... args) {
        return this.queryForList(sql, args, elementType);
    }

    public List<Map<String, Object>> queryForList(String sql, Object... args) {
        return this.queryForList(sql, args, null, null);
    }

    public List<Map<String, Object>> queryForList(String sql, Object[] args, int[] argTypes) {
        final List<Map<String, Object>> result = new ArrayList<>();
        this.query(sql, args, argTypes, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                while (rs.next()) {
                    Map<String, Object> map = new LinkedHashMap<>();
                    for (int i = 1; i <= columnCount; i++) {
                        map.put(metaData.getColumnName(i), rs.getObject(i));
                    }
                    result.add(map);
                }
            }
        });
        return result;
    }

    public <T> List<T> query(final String sql, RowMapper<T> rowMapper) {
        return this.query(new DefaultPreparedStatementCreator(new DefaultPreparedStatementSetter(null, null), sql), rowMapper);
    }

    public void query(String sql, RowCallbackHandler rch) {
        this.query(new DefaultPreparedStatementCreator(new DefaultPreparedStatementSetter(null, null), sql), rch);
    }

    public <T> T query(final String sql, final ResultSetExtractor<T> rse) {
        return this.query(sql, null, null, rse);
    }


    public <T> List<T> query(PreparedStatementCreator psc, RowMapper<T> rowMapper) {
        List<T> result = new ArrayList<>();
        try {
            pr = psc.createPreparedStatement(dataSource.getConnection());
            rs = pr.executeQuery();
            int rowNum = 0;
            while (rs.next()) {
                rowNum++;
                T o = rowMapper.mapRow(rs, rowNum);
                result.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return result;
    }


    public void query(PreparedStatementCreator psc, RowCallbackHandler rch) {
        try {
            pr = psc.createPreparedStatement(dataSource.getConnection());
            rs = pr.executeQuery();
            while (rs.next()) {
                rch.processRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
    }


    public <T> T query(PreparedStatementCreator psc, ResultSetExtractor<T> rse) {
        T result = null;
        try {
            pr = psc.createPreparedStatement(dataSource.getConnection());
            rs = pr.executeQuery();
            result = rse.extractData(rs);
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return result;
    }

    public <T> List<T> query(String sql, PreparedStatementSetter pss, RowMapper<T> rowMapper) {
        return this.query(new DefaultPreparedStatementCreator(pss, sql), rowMapper);
    }

    public <T> List<T> query(String sql, Object[] args, RowMapper<T> rowMapper) {
        return this.query(sql, args, null, rowMapper);
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) {
        return this.query(sql, args, rowMapper);
    }

    public <T> T query(String sql, PreparedStatementSetter pss, ResultSetExtractor<T> rse) {
        return this.query(new DefaultPreparedStatementCreator(pss, sql), rse);
    }


    public <T> T query(String sql, Object[] args, int[] argTypes, ResultSetExtractor<T> rse) {
        T result = null;
        try {
            rs = executeQuery(sql, args, argTypes);
            result = rse.extractData(rs);
        } catch (SQLException | DataAccessException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return result;
    }

    public <T> T query(String sql, Object[] args, ResultSetExtractor<T> rse) {
        return this.query(sql, args, null, rse);
    }


    public <T> List<T> query(String sql, Object[] args, int[] argTypes, RowMapper<T> rowMapper) {
        List<T> result = new ArrayList<>();
        try {
            rs = this.executeQuery(sql, args, argTypes);
            int rowNum = 0;
            while (rs.next()) {
                rowNum++;
                T t = rowMapper.mapRow(rs, rowNum);
                result.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
        return result;
    }

    public <T> T query(String sql, ResultSetExtractor<T> rse, Object... args) {
        return this.query(sql, args, rse);
    }

    public void query(String sql, PreparedStatementSetter pss, RowCallbackHandler rch) {
        this.query(new DefaultPreparedStatementCreator(pss, sql), rch);
    }


    public void query(String sql, Object[] args, int[] argTypes, RowCallbackHandler rch) {
        try {
            rs = this.executeQuery(sql, args, argTypes);
            while (rs.next()) {
                rch.processRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeSource();
        }
    }

    public void query(String sql, Object[] args, RowCallbackHandler rch) {
        this.query(sql, args, null, rch);
    }

    public void query(String sql, RowCallbackHandler rch, Object... args) {
        this.query(sql, args, null, rch);
    }

}

