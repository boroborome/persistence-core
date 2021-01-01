package com.happy3w.persistence.core.filter;

import com.happy3w.toolkits.manager.ConfigManager;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.List;

@Getter
public class CombineFilterProcessor<AT extends Annotation, VT> implements IFilterProcessor<AT, VT> {
    protected ConfigManager<IFilterProcessor> processorConfig = ConfigManager.inherit();
    protected ConfigManager<IFilterProcessor> collectionProcessorConfig = ConfigManager.inherit();

    public <FT> void registProcessor(Class<FT> refType, IFilterProcessor<AT, FT> processor) {
        processorConfig.regist(refType, processor);
    }

    public <IT> void registCollectionProcessor(Class<IT> itemType, IFilterProcessor<AT, Collection<IT>> processor) {
        collectionProcessorConfig.regist(itemType, processor);
    }

    @Override
    public void collectFilters(AT ftAnnotation, VT ref, List<IFilter> filters) {
        IFilterProcessor processor = findProcessorByRefValue(ref);
        if (processor == null) {
            throw new UnsupportedOperationException(getRealAnnotationType(ftAnnotation)
                    + " not support data type:" + ref.getClass());
        }
        processor.collectFilters(ftAnnotation, ref, filters);
    }

    private IFilterProcessor findProcessorByRefValue(VT ref) {
        if (ref instanceof Collection) {
            Collection c = (Collection) ref;
            Object first = c.iterator().next();
            return collectionProcessorConfig.findByType(first.getClass());
        } else {
            return processorConfig.findByType(ref.getClass());
        }
    }

    private Class getRealAnnotationType(Annotation annotation) {
        return annotation.getClass().getInterfaces()[0];
    }
}
