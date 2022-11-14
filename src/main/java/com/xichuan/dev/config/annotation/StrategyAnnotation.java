package com.xichuan.dev.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Xichuan
 * @Date 2022/4/13 10:16
 * @Description
 */

/**
 * 策略注解
 * 将策略注册到StrategyContext
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StrategyAnnotation {
    //策略名称
    String name() default "";
    //策略描述
    String description() default "";
}
