# quality-manage
一个轻量化、客制化的数据质量管理工具，比Griffin更灵活，更轻巧🍰

## 1.数据质量管理项目的初衷
数据稽核到业务开发到一定程度后，就需要提到日程了。在调研后发现，开源的稽核工具只有Griffin。

我们公司目前的数据量比较少，一天的数据量最多也就百万、千万级别，而且需要定义一些复杂的sql来实现稽核.在调研的时候使用Griffin进行测试，发现Griffin的内置rule比较少，如下图：
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141531904.png)

而且配置起来比较蛮烦，上手有点痛苦
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141532489.png)

在运行Griffin的时候还有一些小bug,当时解决这个小bug进行了一个偷巧的操作，就没进行PR,jira的issues地址:https://issues.apache.org/jira/browse/GRIFFIN-248

所以想着手写一个简单的没有界面的稽核工具，只通过sql实现稽核，调度的话就放到airflow中进行每日调度



## 2.项目自定义开发指导

### 2.1 项目运行流程
当运行`QualityApplication`主类的时候，需要加上稽核规则id;主程序会根据稽核规则从`quality_manage_rule`表中读取稽核规则，稽核规则的不通，内部的稽核json都不一样，拿`custom_sql.json`这个稽核规则来说明
```json
{
	"source_type":"SOURCE_MYSQL",
	"sql":"select count(1) as result,case when count(1)>1000000 then ''F'' else ''T'' end as is_pass from test_xichuan_database.dws_xichuan_table lid where date(UPDATE_TIME) = date(date_add(now(),interval -1 day)) "
}
```
`custom_sql.json`稽核规则很简单,一个是数据源类型，一个是稽核的sql。当稽核结束的时候，会将稽核结果写入到`quality_manage_result`表中



### 2.2 数据库表与配置文件说明

**1.创建稽核规则表与稽核结果表**
```sql
-- 稽核规则表
CREATE TABLE `quality_manage_rule` (
  `id` int(11) NOT NULL,
  `name` varchar(64) DEFAULT NULL COMMENT '规则名称',
  `audit_type` varchar(128) DEFAULT NULL COMMENT '稽核类型',
  `strategy` varchar(128) NOT NULL COMMENT '稽核规则',
  `rule_json` text NOT NULL,
  `single_value` varchar(156) DEFAULT NULL COMMENT '单个阈值',
  `range_min` varchar(156) DEFAULT NULL COMMENT '范围值域下限',
  `range_max` varchar(156) DEFAULT NULL COMMENT '范围值域上限',
  `notification_level` int(11) DEFAULT '1' COMMENT '警告级别,1,2,3',
  `description` text NOT NULL COMMENT '规则描述',
  `status` varchar(64) NOT NULL COMMENT '状态,T/F,T执行，F不执行',
  `creat_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8

-- 稽核结果表
CREATE TABLE `quality_manage_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rule_id` int(11) NOT NULL,
  `result` varchar(128) DEFAULT NULL,
  `is_pass` varchar(32) DEFAULT NULL,
  `creat_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=271 DEFAULT CHARSET=utf8


-- 规则示例
insert into quality_manage_rule(id,strategy,rule_json,description,status,range_max)
values(100,'distinct_count','{"source_type":"SOURCE_IMPALA","database":"test_xichuan_database","table":"dws_xichuan_table","column":"td_sequence"}','验证测试','T',null);

insert into quality_manage_rule(id,strategy,rule_json,description,status,range_min,range_max)
values(101,'custom_sql','{"source_type":"SOURCE_MYSQL","sql":"select count(1) as result,case when count(1)>1000000 then ''F'' else ''T'' end as is_pass from test_xichuan_database.dws_xichuan_table lid where date(UPDATE_TIME) = date(date_add(now(),interval -1 day)) "}','验证测试','T',null,null);

```

**2.在default.properties中添加连接配置**

注:`目前的数据源的连接信息是写死的，并没有开发自动识别数据源类型`
```properties
# email
email.smtp=smtp.exmail.qq.com
email.smtp.port=-1
email.from.user=xichuan@123.com
email.to.users=886@123.com,889@123.com
email.login.username=xichuan
email.login.password=xichuan123

# db impala
db.impala.url=jdbc:impala://node01:25004/
db.impala.database=default
db.impala.username=impala
db.impala.password=impala

# db mysql
db.mysql.url=jdbc:mysql://node01:3306/
db.mysql.database=default
db.mysql.username=mysql
db.mysql.password=mysql
```



### 2.3 注解说明

1.**`@RegisterBean`**注解：
`@RegisterBean`注解是将此类注册到容器中管理
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141621150.png)



2.**`@StrategyAnnotation`**注解：
`@StrategyAnnotation`注解写在策略类上，在运行`QualityApplication`主类的时候，可以将策略注册进去
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141623888.png)



3.**`@BeforeExit`**注解：
`@BeforeExit`写在source和sink的close()方法上，这样在程序结束运行前，会自动关闭数据库连接
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141625067.png)





### 2.4 开发新Source说明

目前已经开发好的source有impala和mysql

开发新source其实很简单，拿mysql为例:
1.在`MysqlSource`类上添加`@RegisterBean`注解交给容器管理，将`MysqlSource`类继承`JDBCSource`抽象类
2.实现`open()`与`close()`方法,并在`close()`方法上添加`@BeforeExit`注解，以在程序运行结束前关闭连接
3.在`MysqlSource(JDBCEntity jdbcEntity)`构造方法中调用`open()`方法来创建`Connection`
```java
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
```
4.在`QualityApplication`主类中注册`MysqlSource`
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141640424.png)



### 2.5 开发新Sink说明

目前已经开发好的sink有Console和mysql

开发新JDBC Sink其实很简单，拿mysql为例:
1.在`MysqlAuditSink`类上添加`@RegisterBean`注解交给容器管理，并将`MysqlAuditSink`类继承`JDBCSink`抽象类,并实现Sink接口
2.实现`open()`、`close()`和`write()`方法,并在`close()`方法上添加`@BeforeExit`注解，以在程序运行结束前关闭连接
3.在`MysqlAuditSink(JDBCEntity jdbcEntity)`构造方法中调用`open()`方法来创建`Connection`
```java
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

```
4.在`QualityApplication`主类上注册`MysqlAuditSink`
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141655338.png)



### 2.6 已经有的稽核rule说明
**distinct_count:**
说明:判断某个字段的去重count是否在一个上下范围内,阈值的设置在稽核规则表的`range_min`与`range_max`字段上设置
```json
{
  "source_type":"SOURCE_IMPALA",
  "database":"test_xichuan_database",
  "table":"test_xichuan_table",
  "column":"sequence"
}
```

**total_count:**
说明:判断某字段的总数是否在一个上下范围内,阈值的设置在稽核规则表的`range_min`与`range_max`字段上设置
```json
{
  "source_type":"SOURCE_IMPALA",
  "database":"test_xichuan_database",
  "table":"test_xichuan_table",
  "column":"sequence"
}
```

**custom_sql:**
说明:自定义sql稽核,稽核后的结果字段是`is_pass`,`T`:稽核通过,`F`:稽核不通过
```json
{
  "source_type":"SOURCE_MYSQL",
  "sql":"select count(1) as result,case when count(1)>1000000 then ''F'' else ''T'' end as is_pass from test_xichuan_database.dws_xichuan_table lid where date(UPDATE_TIME) = date(date_add(now(),interval -1 day)) "
}
```



**下面是未完成的稽核规则:**
**null_count:**
说明:空值阈值判断稽核
```json
{
  "source_type":"mysql",
  "database":"mysql_db",
  "table":"",
  "column":""
}
```

**single_value_check:**
说明:单值检测稽核
```json
{
  "source_type":"mysql",
  "database":"mysql_db",
  "table":"",
  "column":""
}
```

**two_value_compare:**
说明:两值比较稽核
```json
{
  "source_type":"mysql",
  "database":"mysql_db",
  "table_one":"",
  "table_one_column":"",
  "table_two":"",
  "table_two_column":""
}
```





### 2.7 开发新稽核rule说明
因为已经封装了稽核规则抽象类，稽核规则开发其实也很简单，我们拿`DistinctCount`这个稽核rule来举例:
1.在`DistinctCount`类上添加`@RegisterBean`与`@StrategyAnnotation`与注解，从而在程序开始运行的时候将该rule注册到稽核容器中
2.继承`AbstractStrategy`抽象类，并实现`parseJson()`方法来解析rule的json规则;实现`resolve()`方法来实现稽核处理,并输出稽核结果
```java
/**
 * @Author Xichuan
 * @Date 2022/4/13 15:55
 * @Description
 */
@RegisterBean
@StrategyAnnotation(name = "distinct_count",description = "distinct数量校验")
public class DistinctCount extends AbstractStrategy<BaseRule> implements Strategy {
    private static Logger logger = LoggerFactory.getLogger(DistinctCount.class);

    /**
     * 解析RuleJson
     * @param ruleJson
     * @return
     */
    @Override
    protected BaseRule parseRuleJson(String ruleJson) {
        return JSON.parseObject(ruleJson,BaseRule.class);
    }

    /**
     * 具体处理逻辑
     * @param auditRule
     * @param sourceFacade
     * @param sinkFacade 输出类
     * @return
     */
    @Override
    public AuditResult resolve(AuditRule auditRule, SourceFacade sourceFacade, SinkFacade sinkFacade) {
        AuditResult auditResult = null;
        try {
            //source
            JDBCSource source = (JDBCSource) sourceFacade.get(rule.getSourceType());
            if (source == null){
                logger.warn("source is null !");
                return auditResult;
            }

            // execute
            String sql = MessageFormat.format("select count(distinct {0}) as ct from {1}.{2}",rule.getColumn(),rule.getDatabase(),rule.getTable());
            Integer count = source.read(sql, rs -> {
                Integer ct = 0;
                while (rs.next()){
                    ct = rs.getInt("ct");
                }
                return ct;
            });

            //sink result
            auditResult = new AuditResult();
            auditResult.setResult(String.valueOf(count));
            auditResult.setIsPass(Constants.RULE_RESULT_PASS);
            auditResult.setRuleId(auditRule.getId());
        } catch (Exception e) {
            logger.error("run distinct_count rule exception！,"+e.getMessage());
            e.printStackTrace();
        }

        return auditResult;
    }
}
```




## 3.项目运行示例
1.运行的参数中加上稽核规则id
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141544270.png)

2.运行程序
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141725350.png)

3.查看mysql中的稽核结果
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141726879.png)

4.简单的邮件告警，会将昨天失败的稽核结果发送到邮箱
![](https://raw.githubusercontent.com/Raray-chuan/xichuan_blog_pic/main/img/202211141724477.png)





## 4.其他

此项目只是简单的实现数据稽核功能，只是一个小小的架子，以避免写一堆重复的稽核代码。项目中还有许多不完善的地方，希望理解，代码虽然不复杂,但希望可以帮助到你






