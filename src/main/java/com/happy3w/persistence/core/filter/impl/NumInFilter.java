package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class NumInFilter<DT extends Number> extends AbstractInFilter<DT> {
    public static final String TYPE =  "num-in";

    public NumInFilter() {
        super(TYPE);
    }
    public NumInFilter(String field, Collection<DT> refs) {
        this(field, refs, true);
    }

    public NumInFilter(String field, Collection<DT> refs, boolean isPositive) {
        super(TYPE, field, refs, isPositive);
    }
}
