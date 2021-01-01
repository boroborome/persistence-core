package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractRangeFilter<DT> extends AbstractSingleFieldFilter {
    protected DT start;
    protected DT end;

    protected boolean includeStart;
    protected boolean includeEnd;

    public AbstractRangeFilter(String type, String field, DT start, DT end) {
        this(type, field, start, end, true, false, true);
    }

    public AbstractRangeFilter(String type, String field, DT start, DT end, boolean isPositive) {
        this(type, field, start, end, true, false, isPositive);
    }

    public AbstractRangeFilter(String type, String field, DT start, DT end, boolean includeStart, boolean includeEnd) {
        this(type, field, start, end, includeStart, includeEnd, true);
    }

    public AbstractRangeFilter(String type, String field, DT start, DT end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(type, field, isPositive);
        this.start = start;
        this.end = end;
        this.includeStart = includeStart;
        this.includeEnd = includeEnd;
    }
}
