package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumRangeFilter<DT extends Number> extends AbstractFieldRangeFilter<DT> {
    public static final String TYPE =  "num-range";

    public NumRangeFilter(String field, DT start, DT end) {
        super(TYPE, field, start, end, true, false, true);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean isPositive) {
        super(TYPE, field, start, end, true, false, isPositive);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean includeStart, boolean includeEnd) {
        super(TYPE, field, start, end, includeStart, includeEnd, true);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(TYPE, field, start, end, includeStart, includeEnd, isPositive);
    }
}
