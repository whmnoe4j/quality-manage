package com.xichuan.dev.entity;

/**
 * @Author Xichuan
 * @Date 2022/4/14 14:01
 * @Description
 */
public class PropsEntity {

    private JDBCEntity mysqlJdbc;
    private JDBCEntity impalaJdbc;


    private String fromEmail;
    private String toEmail;
    private String emailUser;
    private String emailPass;
    private String emailSmtp;
    private int emailSmtpPort;



    @Override
    public String toString() {
        return "PropsEntity{" +
                "mysqlJdbc=" + mysqlJdbc +
                ", impalaJdbc=" + impalaJdbc +
                ", fromEmail='" + fromEmail + '\'' +
                ", toEmail='" + toEmail + '\'' +
                ", emailUser='" + emailUser + '\'' +
                ", emailPass='" + emailPass + '\'' +
                ", emailSmtp='" + emailSmtp + '\'' +
                ", emailSmtpPort='" + emailSmtpPort + '\'' +
                '}';
    }


    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getEmailPass() {
        return emailPass;
    }

    public void setEmailPass(String emailPass) {
        this.emailPass = emailPass;
    }


    public JDBCEntity getMysqlJdbc() {
        return mysqlJdbc;
    }

    public void setMysqlJdbc(JDBCEntity mysqlJdbc) {
        this.mysqlJdbc = mysqlJdbc;
    }

    public JDBCEntity getImpalaJdbc() {
        return impalaJdbc;
    }

    public void setImpalaJdbc(JDBCEntity impalaJdbc) {
        this.impalaJdbc = impalaJdbc;
    }

    public String getEmailSmtp() {
        return emailSmtp;
    }

    public void setEmailSmtp(String emailSmtp) {
        this.emailSmtp = emailSmtp;
    }

    public int getEmailSmtpPort() {
        return emailSmtpPort;
    }

    public void setEmailSmtpPort(int emailSmtpPort) {
        this.emailSmtpPort = emailSmtpPort;
    }
}
