package com.happy3w.persistence.core.rowdata.config;

import com.happy3w.persistence.core.rowdata.obj.RdConfigMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@RdConfigMap(ObjRdConfigImpl.class)
public @interface ObjRdConfig {
    String dateFormatPattern() default "";

    String numFormat() default "";

    String zoneId() default "";
}
