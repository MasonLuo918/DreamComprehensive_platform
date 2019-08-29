package com.Dream.Anntation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解作用的目标
 * 注解会在字节码中存在，在运行时通过反射获取
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeField {

    Class clazz();

    String[] includes() default{};

    String[] excludes() default{};
}
