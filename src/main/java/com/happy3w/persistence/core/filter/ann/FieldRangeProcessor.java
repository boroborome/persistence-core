package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.IFilter;
import com.happy3w.persistence.core.filter.IFilterProcessor;
import com.happy3w.persistence.core.filter.impl.NumRangeFilter;
import com.happy3w.persistence.core.filter.model.NumRange;
import com.happy3w.persistence.core.filter.model.NumRangeExt;

import java.util.List;

public class FieldRangeProcessor extends CombineFilterProcessor<FieldRange, Object> {
    public FieldRangeProcessor() {
        registProcessor(NumRange.class, new NumRangeProcessor());
        registProcessor(NumRangeExt.class, new NumRangeExtProcessor());
    }

    public static class NumRangeProcessor implements IFilterProcessor<FieldRange, NumRange> {

        @Override
        public void collectFilters(FieldRange ftAnnotation, NumRange ref, List<IFilter> filters) {
            filters.add(new NumRangeFilter<>(ftAnnotation.value(), ref.getStart(), ref.getEnd(), ftAnnotation.positive()));
        }
    }

    public static class NumRangeExtProcessor implements IFilterProcessor<FieldRange, NumRangeExt> {

        @Override
        public void collectFilters(FieldRange ftAnnotation, NumRangeExt ref, List<IFilter> filters) {
            filters.add(new NumRangeFilter<>(ftAnnotation.value(),
                    ref.getStart(),
                    ref.getEnd(),
                    ref.isIncludeStart(),
                    ref.isIncludeEnd(),
                    ftAnnotation.positive()));
        }
    }
}
