package com.xichuan.dev.config;

/**
 * @Author Xichuan
 * @Date 2022/4/13 14:42
 * @Description
 */

/**
 * 字典类
 */
public abstract class Constants {
    //Source数据源
    public static final String SOURCE_MYSQL = "SOURCE_MYSQL";
    public static final String SOURCE_IMPALA = "SOURCE_IMPALA";

    //Rule是否开启
    public static final String RULE_FALSE = "F";
    public static final String RULE_TRUE = "T";

    //Rule Result IS_PASE
    public static final String RULE_RESULT_NOT_PASS = "F";
    public static final String RULE_RESULT_PASS = "T";

    //key
    public static final String IMPALA_URL = "db.impala.url";
    public static final String IMPALA_DB = "db.impala.database";
    public static final String IMPALA_USER = "db.impala.username";
    public static final String IMPALA_PASS = "db.impala.password";

    public static final String MYSQL_URL = "db.mysql.url";
    public static final String MYSQL_DB = "db.mysql.database";
    public static final String MYSQL_USER = "db.mysql.username";
    public static final String MYSQL_PASS = "db.mysql.password";

    //email
    public static final String FROM_EMAIL="email.from.user";
    public static final String TO_EMAIL="email.to.users";
    public static final String EMAIL_USER="email.login.username";
    public static final String EMAIL_PASS="email.login.password";
    public static final String EMAIL_SMTP="email.smtp";
    public static final String EMAIL_SMTP_PORT="email.smtp.port";

}
