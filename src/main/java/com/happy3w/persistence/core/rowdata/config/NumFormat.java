package com.happy3w.persistence.core.rowdata.config;

import com.happy3w.persistence.core.rowdata.obj.ObjRdConfigMap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@ObjRdConfigMap(NumFormatImpl.class)
public @interface NumFormat {
    String value();
}
