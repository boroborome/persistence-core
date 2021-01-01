package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DateTimeRangeFilter extends AbstractRangeFilter<Date> {
    public static final String TYPE =  "datetime-range";

    public DateTimeRangeFilter(String field, Date start, Date end) {
        super(TYPE, field, start, end, true, false, true);
    }

    public DateTimeRangeFilter(String field, Date start, Date end, boolean isPositive) {
        super(TYPE, field, start, end, true, false, isPositive);
    }

    public DateTimeRangeFilter(String field, Date start, Date end, boolean includeStart, boolean includeEnd) {
        super(TYPE, field, start, end, includeStart, includeEnd, true);
    }

    public DateTimeRangeFilter(String field, Date start, Date end, boolean includeStart, boolean includeEnd, boolean isPositive) {
        super(TYPE, field, start, end, includeStart, includeEnd, isPositive);
    }
}
