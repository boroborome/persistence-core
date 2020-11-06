package com.happy3w.persistence.core.rowdata;

import java.lang.annotation.Annotation;

public interface IAnnotationRdConfig<T extends IAnnotationRdConfig<T, A>, A extends Annotation> extends IRdConfig <T>{
    void initBy(A annotation);
}
