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
public class MysqlSource extends JDBCSource{
    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public MysqlSource(){
        logger = LoggerFactory.getLogger(MysqlSource.class);
    }

    public MysqlSource(JDBCEntity jdbcEntity)throws Exception{
        logger = LoggerFactory.getLogger(MysqlSource.class);
        open(jdbcEntity);
    }

    @Override
    public MysqlSource open(JDBCEntity jdbcEntity) throws SQLException, ClassNotFoundException {
        logger.debug("open mysql source connection.");
        Class.forName(MYSQL_DRIVER);
        connection = DriverManager.getConnection(jdbcEntity.getUrl()+ jdbcEntity.getDatabase(), jdbcEntity.getUser(), jdbcEntity.getPassword());
        return this;
    }

    @BeforeExit
    @Override
    public void close() {
        logger.debug("close mysql source connection.");
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
