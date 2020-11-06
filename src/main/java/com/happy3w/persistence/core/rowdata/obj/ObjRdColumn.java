package com.happy3w.persistence.core.rowdata.obj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义一个行数据的列
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ObjRdColumn {
    String value();

    String getter() default "";

    String setter() default "";

    boolean required() default true;
}