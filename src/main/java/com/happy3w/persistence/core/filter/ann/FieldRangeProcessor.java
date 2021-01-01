package com.happy3w.persistence.core.filter.ann;

import com.happy3w.persistence.core.filter.CombineFilterProcessor;
import com.happy3w.persistence.core.filter.impl.DateRangeFilter;
import com.happy3w.persistence.core.filter.impl.DateTimeRangeFilter;
import com.happy3w.persistence.core.filter.impl.MonthRangeFilter;
import com.happy3w.persistence.core.filter.impl.NumRangeFilter;
import com.happy3w.persistence.core.filter.model.DateRange;
import com.happy3w.persistence.core.filter.model.DateTimeRange;
import com.happy3w.persistence.core.filter.model.MonthRange;
import com.happy3w.persistence.core.filter.model.NumRange;
import com.happy3w.persistence.core.filter.model.NumRangeExt;

public class FieldRangeProcessor extends CombineFilterProcessor<FieldRange, Object> {
    public FieldRangeProcessor() {
        registProcessor(NumRange.class, (a,r,f) ->
                f.add(new NumRangeFilter(a.value(), r.getStart(), r.getEnd(), a.positive())));
        registProcessor(DateTimeRange.class, (a,r,f) ->
                f.add(new DateTimeRangeFilter(a.value(), r.getStart(), r.getEnd(), a.positive())));
        registProcessor(DateRange.class, (a,r,f) ->
                f.add(new DateRangeFilter(a.value(), r.getStart(), r.getEnd(), a.positive())));
        registProcessor(MonthRange.class, (a, r, f) ->
                f.add(new MonthRangeFilter(a.value(), r.getStart(), r.getEnd(), a.positive())));
        registProcessor(NumRangeExt.class, (a, r, f) ->
                f.add(new NumRangeFilter(a.value(),
                        r.getStart(), r.getEnd(),
                        r.isIncludeStart(), r.isIncludeEnd(),
                        a.positive())));
    }
}
