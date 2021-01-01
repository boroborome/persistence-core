package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AbstractRangeFilter<DT> extends AbstractSingleFieldFilter {
    protected DT start;
    protected boolean includeStart;
    protected DT end;
    protected boolean includeEnd;

    public AbstractRangeFilter(String type) {
        super(type);
    }

    public AbstractRangeFilter(String type,
                               String field,
                               DT start,
                               DT end) {
        this(type, field, start, true, end, false);
    }

    public AbstractRangeFilter(String type,
                               String field,
                               DT start,
                               boolean includeStart,
                               DT end,
                               boolean includeEnd) {
        super(type, field);
        this.start = start;
        this.includeStart = includeStart;
        this.end = end;
        this.includeEnd = includeEnd;
    }
}
