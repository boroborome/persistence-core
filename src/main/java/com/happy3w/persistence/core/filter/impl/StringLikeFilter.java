package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringLikeFilter extends AbstractSingleFieldFilter {
    public static final String TYPE =  "str-like";
    private String ref;

    public StringLikeFilter() {
        super(TYPE);
    }

    public StringLikeFilter(String field, String ftValue) {
        this(field, ftValue, true);
    }

    public StringLikeFilter(String field, String ftValue, boolean isPositive) {
        super(TYPE, field, isPositive);
        this.ref = ftValue;
    }
}
