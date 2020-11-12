package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterProcessor;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;

import java.util.List;

public class FieldLikeProcessor extends CombineFilterProcessor<FieldLike, Object> {
    public FieldLikeProcessor() {
        registProcessor(String.class, new StrLikeProcessor());
    }

    public static class StrLikeProcessor implements IFilterProcessor<FieldLike, String> {

        @Override
        public void collectFilters(FieldLike ftAnnotation, String ftValue, List<IFilter> filters) {
            filters.add(new StringEqualFilter(ftAnnotation.value(), ftValue));
        }
    }
}
