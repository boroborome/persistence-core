package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringEqualFilter extends AbstractSingleFieldFilter {
    public static final String TYPE =  "str-equal";
    private String ref;

    public StringEqualFilter(String field, String ftValue) {
        this(field, ftValue, true);
    }

    public StringEqualFilter(String field, String ftValue, boolean isPositive) {
        super(TYPE);
        this.field = field;
        this.ref = ftValue;
        this.positive = isPositive;
    }
}
