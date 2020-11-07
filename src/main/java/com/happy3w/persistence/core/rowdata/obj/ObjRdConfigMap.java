package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.IAnnotationRdConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE})
public @interface ObjRdConfigMap {
    Class<? extends IAnnotationRdConfig> value();
}
