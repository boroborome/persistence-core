package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumRangeFilter<DT extends Number> extends AbstractSingleFieldFilter {
    public static final String TYPE =  "num-range";

    protected DT start;
    protected DT end;

    protected boolean includeStart;
    protected boolean includeEnd;

    public NumRangeFilter(String field, DT start, DT end) {
        this(field, start, end, true, false, true);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean isPositive) {
        this(field, start, end, true, false, isPositive);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean includeStart, boolean includeEnd) {
        this(field, start, end, includeStart, includeEnd, true);
    }

    public NumRangeFilter(String field, DT start, DT end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(TYPE, field, isPositive);
        this.start = start;
        this.end = end;
        this.includeStart = includeStart;
        this.includeEnd = includeEnd;
    }
}
