package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractStrDateRangeFilter extends AbstractRangeFilter<String> {

    public AbstractStrDateRangeFilter(String type, String field, String start, String end) {
        super(type, field, start, end);
    }

    public AbstractStrDateRangeFilter(String type, String field, String start, String end, boolean isPositive) {
        super(type, field, start, end, isPositive);
    }

    public AbstractStrDateRangeFilter(String type, String field, String start, String end, boolean includeStart, boolean includeEnd) {
        super(type, field, start, end, includeStart, includeEnd);
    }

    public AbstractStrDateRangeFilter(String type, String field, String start, String end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(type, field, start, end, includeStart, includeEnd, isPositive);
    }
}
