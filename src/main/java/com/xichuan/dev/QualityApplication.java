package com.xichuan.dev;

import com.xichuan.dev.audit.Strategy;
import com.xichuan.dev.audit.StrategyContext;
import com.xichuan.dev.config.Constants;
import com.xichuan.dev.config.context.ApplicationContext;
import com.xichuan.dev.entity.AuditRule;
import com.xichuan.dev.entity.JDBCEntity;
import com.xichuan.dev.entity.PropsEntity;
import com.xichuan.dev.sink.AuditSinkFacade;
import com.xichuan.dev.sink.ConsoleAuditSink;
import com.xichuan.dev.sink.MysqlAuditSink;
import com.xichuan.dev.sink.SinkFacade;
import com.xichuan.dev.source.AuditSourceFacade;
import com.xichuan.dev.source.ImaplaSource;
import com.xichuan.dev.source.MysqlSource;
import com.xichuan.dev.source.SourceFacade;
import com.xichuan.dev.util.JDBCUtil;
import com.xichuan.dev.util.NacosApiUtils;
import com.xichuan.dev.util.PropertiesUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * @Author Xichuan
 * @Date 2022/4/13 14:05
 * @Description
 */
public class QualityApplication {
    private static Logger logger = LoggerFactory.getLogger(QualityApplication.class);

    public static void main(String[] args){
        try {
            //ApplicationContext init
            ApplicationContext context = new ApplicationContext(QualityApplication.class.getPackage().getName());

            //ruleID
            String ruleIds = args[0];

            //nacos配置
            PropsEntity props = getProps();

            //Source
            SourceFacade sourceFacade =
                    AuditSourceFacade.getInstance()
                            .register(Constants.SOURCE_MYSQL,new MysqlSource(props.getMysqlJdbc()))
                            .register(Constants.SOURCE_IMPALA,new ImaplaSource(props.getImpalaJdbc()));

            //Sink
            SinkFacade sinkFacade =
                    AuditSinkFacade.getInstance()
                            .addSink(new MysqlAuditSink(props.getMysqlJdbc()))
                            .addSink(new ConsoleAuditSink());

            //切割ruleIds
            if (StringUtils.isNotBlank(ruleIds)){
                String[] ruleSplit = ruleIds.split(",");
                for (String ruleId : ruleSplit){
                    //AuditRule
                    AuditRule auditRule = getAuditRuleByRuleId(props,ruleId);
                    if (!judgeRule(auditRule,ruleId)){
                        continue;
                    }
                    //匹配对应的策略
                    Strategy strategy = StrategyContext.getStrategy(auditRule.getStrategy());
                    if (strategy == null){
                        logger.warn("Not found strategy='{}'！",auditRule.getStrategy());
                        return;
                    }
                    strategy.deal(auditRule,sourceFacade, sinkFacade);
                }

            }

            //ApplicationContext close
            context.close();
            //exit jvm
            System.exit(0);
        } catch (Exception exception) {
            logger.error("Program failed to run！,"+exception.getMessage());
            exception.printStackTrace();
        }

    }

    /**
     * judge rule
     * @param auditRule
     * @param ruleId
     * @return
     */
    private static Boolean judgeRule(AuditRule auditRule,String ruleId){
        if (auditRule == null || StringUtils.isBlank(auditRule.getRuleJson())){
            logger.error("rule_id={},Failed to obtain the rule！",ruleId);
            return false;
        }
        if (!Constants.RULE_TRUE.equals(auditRule.getStatus())){
            logger.error("This rule is not enabled,rule_id={}！",ruleId);
            return false;
        }
        return true;
    }

    /**
     * 获取nacos配置
     * @return
     * @throws Exception
     */
    private static PropsEntity getProps()throws Exception{
        Properties properties = PropertiesUtils.loadProperties(null);

        JDBCEntity impalaJdbc = new JDBCEntity();
        impalaJdbc.setUrl(properties.getProperty(Constants.IMPALA_URL));
        impalaJdbc.setDatabase(properties.getProperty(Constants.IMPALA_DB));
        impalaJdbc.setUser(properties.getProperty(Constants.IMPALA_USER));
        impalaJdbc.setPassword(properties.getProperty(Constants.IMPALA_PASS));

        JDBCEntity mysqlJdbc = new JDBCEntity();
        mysqlJdbc.setUrl(properties.getProperty(Constants.MYSQL_URL));
        mysqlJdbc.setDatabase(properties.getProperty(Constants.MYSQL_DB));
        mysqlJdbc.setUser(properties.getProperty(Constants.MYSQL_USER));
        mysqlJdbc.setPassword(properties.getProperty(Constants.MYSQL_PASS));

        PropsEntity propsEntity = new PropsEntity();
        propsEntity.setImpalaJdbc(impalaJdbc);
        propsEntity.setMysqlJdbc(mysqlJdbc);

        return propsEntity;
    }

    /**
     * 根据ruleId,获取rule规则
     * @param ruleId
     * @return
     */
    private static AuditRule getAuditRuleByRuleId(PropsEntity nacos, String ruleId) throws Exception{
        JDBCEntity mysqlJdbc = nacos.getMysqlJdbc();
        if(mysqlJdbc == null || StringUtils.isBlank(ruleId)){
            return null;
        }

        AuditRule auditRule = new AuditRule();
        if (StringUtils.isBlank(mysqlJdbc.getDriver())){
            mysqlJdbc.setDriver("com.mysql.jdbc.Driver");
        }

        String sql = "select id,strategy,rule_json,status,range_min,range_max,notification_level from quality_manage_rule where id = '"+ruleId+"'";
        logger.debug("query rule sql:{}",sql);
        Connection connection = JDBCUtil.getConnection(mysqlJdbc.getDriver(),mysqlJdbc.getUrl()+mysqlJdbc.getDatabase(),mysqlJdbc.getUser(),mysqlJdbc.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            auditRule.setId(resultSet.getInt("id"));
            auditRule.setStrategy(resultSet.getString("strategy"));
            auditRule.setRuleJson(resultSet.getString("rule_json"));
            auditRule.setStatus(resultSet.getString("status"));
            auditRule.setRangeMin(resultSet.getString("range_min"));
            auditRule.setRangeMax(resultSet.getString("range_max"));
            auditRule.setNotificationLevel(resultSet.getInt("notification_level"));
        }

        JDBCUtil.close(resultSet,preparedStatement,connection);

        //根据nacos的库名进行修改
        String ruleJson = auditRule.getRuleJson();
        if (StringUtils.isNotBlank(ruleJson)){
            auditRule.setRuleJson(ruleJson);
        }

        return auditRule;
    }
}
