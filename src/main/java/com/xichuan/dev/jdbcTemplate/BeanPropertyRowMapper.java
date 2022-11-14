package com.xichuan.dev.jdbcTemplate;


import com.xichuan.dev.jdbcTemplate.util.BeanUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/**
 * @Author Xichuan
 * @Date 2022/4/15 9:55
 * @Description
 */

/**
 * 利用反射获取Bean类型
 * @param <T>
 */
public class BeanPropertyRowMapper<T> implements RowMapper<T> {
    private Class<T> beanClass;
    private Map<String,Field> mappedFields;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BeanPropertyRowMapper(Class<T> beanClass) {
        this.beanClass = beanClass;
        this.initialize(beanClass);
    }

    /**
     * 转全小写
     * @param fieldName
     * @return
     */
    private String lowCaseName(String fieldName) {
        return fieldName.toLowerCase();
    }

    /**
     * 驼峰转下划线
     * @param camelName
     * @return
     */
    private String underscoreName(String camelName) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camelName.length(); i++) {
            char c = camelName.charAt(i);
            if(c >= 65 && c <= 90) {
                sb.append("_");
                while(true) {
                    c = camelName.charAt(i);
                    if(c <= 65 || c >= 90) {
                        sb.append(c);
                        break;
                    }
                    sb.append((char)(c+32));
                    i++;
                }
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 初始化，获取映射map
     * @param beanClass
     */
    private void initialize(Class<T> beanClass) {
        Field[] declaredFields = beanClass.getDeclaredFields();
        mappedFields = new HashMap<>();
        for (Field declaredField : declaredFields) {
            String fieldName = declaredField.getName();
            String lowCaseName = this.lowCaseName(fieldName);
            String underscoreName = this.underscoreName(fieldName);
            mappedFields.put(fieldName,declaredField);
            if(!lowCaseName.equals(underscoreName)) {
                mappedFields.put(underscoreName,declaredField);
            }
            mappedFields.put(lowCaseName,declaredField);
        }
    }

    @Override
    public T mapRow(ResultSet rs, int rowNum) throws SQLException {
        T t = null;
        try {
            t = beanClass.newInstance();
            ResultSetMetaData md = rs.getMetaData();
            int columnCount = md.getColumnCount();
            for (int i = 1; i <= columnCount ; i++) {
                String columnName = md.getColumnName(i);
                Field field = mappedFields.get(columnName);
                if(field == null) {
                    this.logger.debug("No property found for column '" + columnName + "' mapped to field '" + columnName + "'");
                    System.out.println("列名对应的属性不存在");
                }else {
                    Method writeMethod = BeanUtil.getWriteMethod(field);
                    writeMethod.invoke(t,rs.getObject(i));
                }
            }
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
        return t;
    }
}
