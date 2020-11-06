package com.happy3w.persistence.core.rowdata.obj;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ObjRdUnknownColumn {
    Strategy strategy() default Strategy.ignore;

    enum Strategy {
        ignore,
        error
    }
}
