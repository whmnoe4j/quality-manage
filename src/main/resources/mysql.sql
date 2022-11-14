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







