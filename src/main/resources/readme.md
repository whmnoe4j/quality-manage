
# 每一个稽核规则的json说明

## 已完成
### distinct_count
{
    "source_type":"SOURCE_IMPALA",
    "database":"test_xac_dws",
    "table":"dws_proxy_oee",
    "column":"td_sequence"
}

### total_count
{
"source_type":"SOURCE_IMPALA",
"database":"test_xac_dws",
"table":"dws_proxy_oee",
"column":"td_sequence"
}

### custom_sql
{
"source_type":"SOURCE_IMPALA",
"sql":""
}


## 未完成
### null_count
{
    "source_type":"mysql",
    "database":"mysql_db",
    "table":"",
    "column":""
}

### single_value_check
{
    "source_type":"mysql",
    "database":"mysql_db",
    "table":"",
    "column":""
}


### two_value_compare
{
    "source_type":"mysql",
    "database":"mysql_db",
    "table_one":"",
    "table_one_column":"",
    "table_two":"",
    "table_two_column":""
}

