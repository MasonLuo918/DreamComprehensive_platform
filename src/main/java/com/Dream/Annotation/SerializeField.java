package com.Dream.Annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解作用的目标
 * 注解会在字节码中存在，在运行时通过反射获取
 */

/**
 * clazz:注解
 * includes:需要的属性
 * excludes:不需要的属性
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeField {

    Class clazz();

    String[] includes() default{};

    String[] excludes() default{};
}
