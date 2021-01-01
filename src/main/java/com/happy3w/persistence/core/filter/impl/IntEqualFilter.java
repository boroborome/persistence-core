package com.happy3w.persistence.core.filter.impl;

public class IntEqualFilter extends AbstractEqualFilter<Integer> {
    public static final String TYPE =  "int-equal";

    public IntEqualFilter(String field, Integer ref) {
        this(field, ref, true);
    }

    public IntEqualFilter(String field, Integer ref, boolean isPositive) {
        super(TYPE, field, ref, isPositive);
    }
}
