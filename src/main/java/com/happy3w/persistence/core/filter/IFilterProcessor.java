package com.happy3w.persistence.core.filter;

import java.lang.annotation.Annotation;
import java.util.List;

public interface IFilterProcessor<AT extends Annotation, VT> {
    void collectFilters(AT ftAnnotation, VT ftValue, List<IFilter> filters);
}
