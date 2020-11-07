package com.happy3w.persistence.core.rowdata.config;

import com.happy3w.persistence.core.rowdata.obj.ObjRdConfigMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ObjRdConfigMap(ObjRdConfigImpl.class)
public @interface ObjRdConfig {
    String dateFormatPattern() default "";

    String numFormat() default "";

    String zoneId() default "";
}
