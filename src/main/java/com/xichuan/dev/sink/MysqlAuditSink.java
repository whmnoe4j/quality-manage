package com.xichuan.dev.sink;

import com.xichuan.dev.config.annotation.BeforeExit;
import com.xichuan.dev.config.annotation.RegisterBean;
import com.xichuan.dev.entity.AuditResult;
import com.xichuan.dev.entity.JDBCEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author Xichuan
 * @Date 2022/4/13 14:17
 * @Description
 */

/**
 * Mysql Sink
 */
@RegisterBean
public class MysqlAuditSink extends JdbcSink implements Sink<AuditResult> {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public MysqlAuditSink(){}

    public MysqlAuditSink(JDBCEntity jdbcEntity)throws Exception{
        open(jdbcEntity);
    }

    @Override
    public MysqlAuditSink open(JDBCEntity jdbcEntity) throws SQLException, ClassNotFoundException {
        logger.debug("open mysql audit sink connection.");
        Class.forName(MYSQL_DRIVER);
        connection = DriverManager.getConnection(jdbcEntity.getUrl()+ jdbcEntity.getDatabase(), jdbcEntity.getUser(), jdbcEntity.getPassword());
        return this;
    }

    public boolean write(JDBCEntity jdbcEntity,AuditResult auditResult) {
        Boolean executeFlag = false;
        try {
            if (connection == null){
                open(jdbcEntity);
            }
            executeFlag = this.write(auditResult);
        } catch (Exception e) {
            logger.error("writes the result to Mysql and reports an error！！," + e.getMessage());
            e.printStackTrace();
        }
        return executeFlag;
    }

    @Override
    public boolean write(AuditResult auditResult) {
        Boolean executeFlag = false;
        try {

            if (connection == null){
                logger.error("Mysql connection is not initialized！");
                return false;
            }

            if (auditResult == null){
                logger.warn("AuditResult is null!");
                return false;
            }

            String sql = joinSql(auditResult);
            logger.info("audit mysql sink sql:"+sql);
            if (StringUtils.isNotBlank(sql)){
                executeFlag = writeToDB(sql);
            }

        } catch (Exception e) {
            logger.error("writes the result to Mysql and reports an error！," + e.getMessage());
            e.printStackTrace();
        }

        return executeFlag;
    }

    /**
     * 拼接sql
     * @param auditResult
     * @return
     */
    private String joinSql(AuditResult auditResult){
        String sql = "";
        if (auditResult != null){
            sql = "insert into quality_manage_result(rule_id,result,is_pass) values('"+auditResult.getRuleId()+"','"+auditResult.getResult()+"','"+auditResult.getIsPass()+"')";
        }
        return sql;
    }

    @BeforeExit
    @Override
    public void close() {
        logger.debug("close mysql audit sink connection.");
        try {
            if (connection != null){
                connection.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
