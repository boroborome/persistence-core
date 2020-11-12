package com.happy3w.persistence.core.filter;

import com.happy3w.toolkits.utils.MapUtils;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CombineFilterProcessor<AT extends Annotation, VT> implements IFilterProcessor<AT, VT> {
    protected Map<Class, IFilterProcessor> ftValueTypeToProcessor = new HashMap<>();

    public void registProcessor(Class ftValueType, IFilterProcessor processor) {
        ftValueTypeToProcessor.put(ftValueType, processor);
    }
    @Override
    public void collectFilters(AT ftAnnotation, VT ftValue, List<IFilter> filters) {
        IFilterProcessor processor = MapUtils.findByType(ftValue.getClass(), ftValueTypeToProcessor);
        if (processor == null) {
            throw new UnsupportedOperationException(getRealAnnotationType(ftAnnotation)
                    + " not support data type:" + ftValue.getClass());
        }
        processor.collectFilters(ftAnnotation, ftValue, filters);
    }

    private Class getRealAnnotationType(Annotation annotation) {
        return annotation.getClass().getInterfaces()[0];
    }
}
