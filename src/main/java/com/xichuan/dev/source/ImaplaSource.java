package com.xichuan.dev.source;

import com.xichuan.dev.config.annotation.BeforeExit;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.entity.JDBCEntity;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/13 18:42
 * @Description
 */
@RegisterBean
public class ImaplaSource extends JDBCSource{
    private static final String IMPALA_DRIVER = "com.cloudera.impala.jdbc41.Driver";

    public ImaplaSource(){
        logger = LoggerFactory.getLogger(ImaplaSource.class);
    }

    public ImaplaSource(JDBCEntity jdbcEntity)throws Exception{
        logger = LoggerFactory.getLogger(ImaplaSource.class);
        open(jdbcEntity);
    }

    @Override
    public ImaplaSource open(JDBCEntity jdbcEntity) throws SQLException, ClassNotFoundException {
        logger.debug("open impala source connection.");
        Class.forName(IMPALA_DRIVER);
        connection = DriverManager.getConnection(jdbcEntity.getUrl()+ jdbcEntity.getDatabase(), jdbcEntity.getUser(), jdbcEntity.getPassword());
        return this;
    }


    @BeforeExit
    @Override
    public void close() {
        logger.debug("close impala source connection.");
        try {
            if (rs != null){
                rs.close();
            }
            if (connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
