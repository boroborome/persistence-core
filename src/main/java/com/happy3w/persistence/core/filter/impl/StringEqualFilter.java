package com.happy3w.persistence.core.filter.impl;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringEqualFilter extends AbstractEqualFilter<String> {
    public static final String TYPE =  "str-equal";

    public StringEqualFilter() {
        super(TYPE);
    }

    public StringEqualFilter(String field, String ftValue) {
        this(field, ftValue, true);
    }

    public StringEqualFilter(String field, String ftValue, boolean isPositive) {
        super(TYPE, field, ftValue, isPositive);
    }
}
