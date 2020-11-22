package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class StringInFilter extends AbstractSingleFieldFilter {
    public static final String TYPE =  "str-in";
    private Collection<String> refs;

    public StringInFilter(String field, Collection<String> ftValues) {
        this(field, ftValues, true);
    }

    public StringInFilter(String field, Collection<String> ftValues, boolean isPositive) {
        super(TYPE);
        this.field = field;
        this.refs = ftValues;
        this.positive = isPositive;
    }
}
