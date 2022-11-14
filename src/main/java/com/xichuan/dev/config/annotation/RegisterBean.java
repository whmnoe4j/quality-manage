package com.xichuan.dev.config.annotation;

/**
 * @Author Xichuan
 * @Date 2022/4/13 11:01
 * @Description
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * bean注册注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RegisterBean {
    // bean名称
    String name() default "";
    //是否将此类加载到ApplicationContext
    boolean isLoad() default true;
}
