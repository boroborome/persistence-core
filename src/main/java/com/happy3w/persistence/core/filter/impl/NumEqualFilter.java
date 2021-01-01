package com.happy3w.persistence.core.filter.impl;

public class NumEqualFilter<DT extends Number> extends AbstractEqualFilter<DT> {
    public static final String TYPE =  "num-equal";

    public NumEqualFilter(String field, DT ref) {
        this(field, ref, true);
    }

    public NumEqualFilter(String field, DT ref, boolean isPositive) {
        super(TYPE, field, ref, isPositive);
    }
}
