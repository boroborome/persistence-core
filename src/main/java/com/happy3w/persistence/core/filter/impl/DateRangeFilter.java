package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DateRangeFilter extends AbstractStrDateRangeFilter {
    public static final String TYPE =  "date-range";

    public DateRangeFilter() {
        super(TYPE);
    }

    public DateRangeFilter(String field, String start, String end) {
        super(TYPE, field, start, end, true, false, true);
    }

    public DateRangeFilter(String field, String start, String end, boolean isPositive) {
        super(TYPE, field, start, end, true, false, isPositive);
    }

    public DateRangeFilter(String field, String start, String end, boolean includeStart, boolean includeEnd) {
        super(TYPE, field, start, end, includeStart, includeEnd, true);
    }

    public DateRangeFilter(String field, String start, String end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(TYPE, field, start, end, includeStart, includeEnd, isPositive);
    }

    public DateRangeFilter(String field, String start, String end, boolean includeStart, boolean includeEnd, String zoneId, boolean isPositive) {
        super(TYPE, field, start, end, includeStart, includeEnd, zoneId, isPositive);
    }
}
