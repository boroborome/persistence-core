package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterProcessor;
import com.happy3w.persistence.core.filter.impl.NumEqualFilter;
import com.happy3w.persistence.core.filter.impl.NumInFilter;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;
import com.happy3w.persistence.core.filter.impl.StringInFilter;

import java.util.Collection;
import java.util.List;

public class FieldEqualProcessor extends CombineFilterProcessor<FieldEqual, Object> {
    public FieldEqualProcessor() {
        registProcessor(String.class, new StrEqualProcessor());
        registProcessor(Number.class, new NumEqualProcessor());
        registCollectionProcessor(String.class, new StrInProcessor());
        registCollectionProcessor(Number.class, new NumInProcessor());
    }

    public static class StrEqualProcessor implements IFilterProcessor<FieldEqual, String> {

        @Override
        public void collectFilters(FieldEqual ftAnnotation, String ref, List<IFilter> filters) {
            filters.add(new StringEqualFilter(ftAnnotation.value(), ref, ftAnnotation.positive()));
        }
    }

    public static class NumEqualProcessor implements IFilterProcessor<FieldEqual, Number> {

        @Override
        public void collectFilters(FieldEqual ftAnnotation, Number ref, List<IFilter> filters) {
            filters.add(new NumEqualFilter(ftAnnotation.value(), ref, ftAnnotation.positive()));
        }
    }

    public static class StrInProcessor implements IFilterProcessor<FieldEqual, Collection> {

        @Override
        public void collectFilters(FieldEqual ftAnnotation, Collection ref, List<IFilter> filters) {
            filters.add(new StringInFilter(ftAnnotation.value(), ref, ftAnnotation.positive()));
        }
    }

    public static class NumInProcessor implements IFilterProcessor<FieldEqual, Collection> {

        @Override
        public void collectFilters(FieldEqual ftAnnotation, Collection ref, List<IFilter> filters) {
            filters.add(new NumInFilter<>(ftAnnotation.value(), ref, ftAnnotation.positive()));
        }
    }
}
