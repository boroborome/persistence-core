package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class StringInFilter extends AbstractInFilter<String> {
    public static final String TYPE =  "str-in";

    public StringInFilter() {
        super(TYPE);
    }

    public StringInFilter(String field, Collection<String> refs) {
        this(field, refs, true);
    }

    public StringInFilter(String field, Collection<String> refs, boolean isPositive) {
        super(TYPE, field, refs, isPositive);
    }
}
