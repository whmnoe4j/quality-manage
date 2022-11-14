package com.xichuan.dev.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Xichuan
 * @Date 2022/4/13 17:58
 * @Description
 */

/**
 * 程序退出前的方法注解
 * 在程序退出之前，会执行该方法
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeExit {
}
