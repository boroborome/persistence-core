package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterProcessor;
import com.happy3w.persistence.core.filter.impl.StringLikeFilter;
import com.happy3w.persistence.core.filter.impl.StringLikeInFilter;

import java.util.Collection;
import java.util.List;

public class FieldLikeProcessor extends CombineFilterProcessor<FieldLike, Object> {
    public FieldLikeProcessor() {
        registProcessor(String.class, new StrLikeProcessor());
        registProcessor(Collection.class, new StrLikeInProcessor());
    }

    public static class StrLikeProcessor implements IFilterProcessor<FieldLike, String> {

        @Override
        public void collectFilters(FieldLike ftAnnotation, String ftValue, List<IFilter> filters) {
            filters.add(new StringLikeFilter(ftAnnotation.value(), ftValue));
        }
    }

    public static class StrLikeInProcessor implements IFilterProcessor<FieldLike, Collection> {

        @Override
        public void collectFilters(FieldLike ftAnnotation, Collection ftValues, List<IFilter> filters) {
            filters.add(new StringLikeInFilter(ftAnnotation.value(), ftValues));
        }
    }
}
