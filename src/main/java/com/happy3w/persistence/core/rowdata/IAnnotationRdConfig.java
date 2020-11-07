package com.happy3w.persistence.core.rowdata;

import java.lang.annotation.Annotation;

public interface IAnnotationRdConfig<A extends Annotation> extends IRdConfig {
    void initBy(A annotation);
}
