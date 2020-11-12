package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterProcessor;
import com.happy3w.persistence.core.filter.impl.StringEqualFilter;

import java.util.List;

public class FieldEqualProcessor extends CombineFilterProcessor<FieldEqual, Object> {
    public FieldEqualProcessor() {
        registProcessor(String.class, new StrEqualProcessor());
    }

    public static class StrEqualProcessor implements IFilterProcessor<FieldEqual, String> {

        @Override
        public void collectFilters(FieldEqual ftAnnotation, String ftValue, List<IFilter> filters) {
            filters.add(new StringEqualFilter(ftAnnotation.value(), ftValue));
        }
    }
}
