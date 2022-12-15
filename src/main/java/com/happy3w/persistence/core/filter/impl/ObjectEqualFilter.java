package com.happy3w.persistence.core.filter.impl;

public class ObjectEqualFilter extends AbstractEqualFilter<Object> {
    public static final String TYPE =  "obj-equal";

    public ObjectEqualFilter() {
        super(TYPE);
    }

    public ObjectEqualFilter(String field, Object ref) {
        this(field, ref, true);
    }

    public ObjectEqualFilter(String field, Object ref, boolean isPositive) {
        super(TYPE, field, ref, isPositive);
    }
}
