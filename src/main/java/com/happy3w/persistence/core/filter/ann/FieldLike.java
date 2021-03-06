package com.happy3w.persistence.core.filter.ann;


import com.happy3w.persistence.core.filter.FilterProcessor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
@FilterProcessor(FieldLikeProcessor.class)
public @interface FieldLike {
    String value();
    boolean positive() default true;
}
