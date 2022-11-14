package com.xichuan.dev.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @Author Xichuan
 * @Date 2022/4/18 11:21
 * @Description
 */
public class SendMailUtil{
    private Logger logger = LoggerFactory.getLogger(SendMailUtil.class);

    //发送邮箱地址
    private  String from = "";
    //发送人
    private  String fromName = "";
    //用户名
    private  String username = "";
    //密码
    private  String password = "";
    //编码格式
    private  String charSet = "";

    private String smtp = "";

    private int smtpPort = -1;

    public static String HTML_CONTENT_TEMPLATE = "<!DOCTYPE html>\n" +
            "<html lang=\"zh\">\n" +
            "<head>\n" +
            "  <title>demo</title>\n" +
            "<style type=\"text/css\">\n" +
            "        table.reference, table.tecspec {\n" +
            "            border-collapse: collapse;\n" +
            "            width: 100%;\n" +
            "            margin-bottom: 4px;\n" +
            "            margin-top: 4px;\n" +
            "            position: relative;\n" +
            "        }\n" +
            "        table.reference th {\n" +
            "            border: 1px solid #555;\n" +
            "            font-size: 12px;\n" +
            "            padding: 3px;\n" +
            "            vertical-align: top;\n" +
            "        }\n" +
            "        table.reference td {\n" +
            "            line-height: 2em;\n" +
            "            min-width: 24px;\n" +
            "            border: 1px solid #555;\n" +
            "            padding: 5px;\n" +
            "            padding-top: 7px;\n" +
            "            padding-bottom: 7px;\n" +
            "            vertical-align: top;\n" +
            "        }\n" +
            "        .article-body h3 {\n" +
            "            font-size: 1.8em;\n" +
            "            margin: 2px 0;\n" +
            "            line-height: 1.8em;\n" +
            "        }\n" +
            "    </style>" +
            "</head>\n" +
            "<body>\n" +
            "  <table class=\"reference\">\n" +
            "    <tbody>\n" +
            "      <tr>\n" +
            "        <th>\n" +
            "          <div>rule_id</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>name</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>audit_type</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>strategy</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>result</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>is_pass</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>run_time</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>rule_json</div>\n" +
            "        </th>\n" +
            "        <th>\n" +
            "          <div>description</div>\n" +
            "        </th>\n" +
            "      </tr>\n" +
            "{0}" +
            "    </tbody>\n" +
            "   </table>\n" +
            "</body>";

    public static String EMAIL_DETAIL_CONTENT =
            "      <tr>\n" +
                    "        <th>\n" +
                    "          {0}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {1}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {2}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {3}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {4}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {5}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {6}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {7}\n" +
                    "        </th>\n" +
                    "        <th>\n" +
                    "          {8}\n" +
                    "        </th>\n" +
                    "      </tr>\n ";

    /**
     * 构造方法
     * @param from
     * @param fromName
     * @param username
     * @param password
     * @param charSet
     */
    public SendMailUtil(String from,String fromName,String username,String password,String smtp,int smtpPort,String charSet) {
        this.from = from;
        this.fromName = fromName;
        this.username = username;
        this.password = password;
        this.charSet = charSet;
        this.smtp = smtp;
        this.smtpPort = smtpPort;
    }


    /**
     * 发送普通邮件
     * @param toMailAddr 收信人地址
     * @param subject email主题
     * @param message 发送email信息
     */
    public void sendCommonMail(List<String> toMailAddr, String subject, String message) {
        HtmlEmail hemail = new HtmlEmail();
        try {
            hemail.setHostName(smtp);
            if (smtpPort != -1){
                hemail.setSmtpPort(smtpPort);
            }
            hemail.setCharset(charSet);
            for (String to : toMailAddr){
                hemail.addTo(to);
            }
            hemail.setFrom(from, fromName);
            hemail.setAuthentication(username, password);
            hemail.setSubject(subject);
            hemail.setHtmlMsg(message);
            hemail.send();
            logger.info("email send true!");
        } catch (Exception e) {
            logger.error("email send error!" + e.getMessage());
            e.printStackTrace();
        }
    }

}