package com.xichuan.dev.jdbcTemplate.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author Xichuan
 * @Date 2022/4/15 9:54
 * @Description
 */
public class BeanUtil {

    public static Method getWriteMethod(Field field) {
        Method method = null;
        try {
            String fieldName = field.getName();
            String writeMethodName = "set"+fieldName.substring(0,1).toUpperCase()+ fieldName.substring(1);
            Class<?> declaringClass = field.getDeclaringClass();
            method = declaringClass.getMethod(writeMethodName, field.getType());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }
}
