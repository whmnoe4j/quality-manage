package com.xichuan.dev;


import com.xichuan.dev.config.Constants;
import com.xichuan.dev.entity.EmailContent;
import com.xichuan.dev.entity.JDBCEntity;
import com.xichuan.dev.entity.PropsEntity;
import com.xichuan.dev.util.JDBCUtil;
import com.xichuan.dev.util.NacosApiUtils;
import com.xichuan.dev.util.PropertiesUtils;
import com.xichuan.dev.util.SendMailUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author Xichuan
 * @Date 2022/4/18 11:02
 * @Description
 */
public class EmailAlertApplication {
    private static Logger logger = LoggerFactory.getLogger(EmailAlertApplication.class);

    public static void main(String [] args) {
        try {
            //获取nacos的配置
            PropsEntity props = getProps();

            //获取mysql中当天不通过的集合
            List<EmailContent> emailContents = getResult(props);

            //构建消息体
            String msg = createEmailContent(emailContents);

            //邮件发送
            List<String> toEmails = Arrays.asList(props.getToEmail().split(","));
            SendMailUtil smu = new SendMailUtil(props.getFromEmail(),"data-quality-center",props.getEmailUser(),props.getEmailPass(),props.getEmailSmtp(),props.getEmailSmtpPort(),"UTF-8");
            //测试发送普通邮件
            smu.sendCommonMail(toEmails, "data quality alert", msg);


        } catch (Exception exception) {
            logger.error("send email fail!");
            exception.printStackTrace();
        }



    }

    /**
     * 获取稽核结果
     * @param nacos
     * @return
     * @throws Exception
     */
    public static List<EmailContent> getResult(PropsEntity nacos) throws Exception{
        JDBCEntity mysqlJdbc = nacos.getMysqlJdbc();
        if (mysqlJdbc == null){
            logger.warn("fetch mysql connection fail!");
            return null;
        }

        if (StringUtils.isBlank(mysqlJdbc.getDriver())){
            mysqlJdbc.setDriver("com.mysql.jdbc.Driver");
        }

        String sql = "select t1.rule_id ,t1.`result`,t1.creat_time,t1.is_pass,t2.strategy ,t2.name,t2.audit_type,t2.rule_json ,t2.description \n" +
                "from offline_analysis_dev.quality_manage_result t1 \n" +
                "left join offline_analysis_dev.quality_manage_rule t2 \n" +
                "on t1.rule_id =t2.id\n" +
                "where DATE_FORMAT( t1.creat_time,'%m-%d-%Y') = DATE_FORMAT( CURDATE(),'%m-%d-%Y')\n" +
                "and (UPPER(t1.is_pass) ='F' or UPPER(t1.is_pass) = UPPER('false') ) ";

        logger.debug("query result sql:{}",sql);
        Connection connection = JDBCUtil.getConnection(mysqlJdbc.getDriver(),mysqlJdbc.getUrl()+mysqlJdbc.getDatabase(),mysqlJdbc.getUser(),mysqlJdbc.getPassword());
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<EmailContent> emailContents = new ArrayList<>();
        EmailContent content = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        while (resultSet.next()){
            content = new EmailContent();
            content.setRuleId(String.valueOf(resultSet.getInt("rule_id")));
            content.setName(resultSet.getString("name"));
            content.setAuditType(resultSet.getString("audit_type"));
            content.setStrategy(resultSet.getString("strategy"));
            content.setRuleJson(resultSet.getString("rule_json"));
            content.setResult(resultSet.getString("result"));
            content.setIsPass(resultSet.getString("is_pass"));
            content.setDescription(resultSet.getString("description"));
            content.setCreateTime(sdf.format(new Date(resultSet.getTimestamp("creat_time").getTime())));
            emailContents.add(content);
        }

        JDBCUtil.close(resultSet,preparedStatement,connection);



        return emailContents;
    }

    /**
     * 拼接内容字符串
     * @param emailContents
     * @return
     */
    public static String createEmailContent(List<EmailContent> emailContents){
        String result = " ";
        for (EmailContent content:emailContents){
            result += MessageFormat.format(SendMailUtil.EMAIL_DETAIL_CONTENT
                    ,content.getRuleId()
                    ,content.getName() == null ? "" : content.getName()
                    ,content.getAuditType() == null ? "" : content.getAuditType()
                    ,content.getStrategy()
                    ,content.getResult()
                    ,content.getIsPass()
                    ,content.getCreateTime()
                    ,content.getRuleJson()
                    ,content.getDescription());
        }
        return SendMailUtil.HTML_CONTENT_TEMPLATE.replace("{0}",result);
    }

    /**
     * 获取nacos配置
     * @return
     * @throws Exception
     */
    private static PropsEntity getProps()throws Exception{
        Properties properties = PropertiesUtils.loadProperties(null);

        PropsEntity propsEntity = new PropsEntity();

        JDBCEntity mysqlJdbc = new JDBCEntity();
        mysqlJdbc.setUrl(properties.getProperty(Constants.MYSQL_URL));
        mysqlJdbc.setDatabase(properties.getProperty(Constants.MYSQL_DB));
        mysqlJdbc.setUser(properties.getProperty(Constants.MYSQL_USER));
        mysqlJdbc.setPassword(properties.getProperty(Constants.MYSQL_PASS));

        propsEntity.setMysqlJdbc(mysqlJdbc);
        propsEntity.setFromEmail(properties.getProperty(Constants.FROM_EMAIL));
        propsEntity.setToEmail(properties.getProperty(Constants.TO_EMAIL));
        propsEntity.setEmailUser(properties.getProperty(Constants.EMAIL_USER));
        propsEntity.setEmailPass(properties.getProperty(Constants.EMAIL_PASS));
        propsEntity.setEmailSmtp(properties.getProperty(Constants.EMAIL_SMTP));
        propsEntity.setEmailSmtpPort(Integer.valueOf(properties.getProperty(Constants.EMAIL_SMTP_PORT)));

        return propsEntity;
    }
}
