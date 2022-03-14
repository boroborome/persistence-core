package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractFilter;
import com.happy3w.persistence.core.filter.IFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class CombineFilter extends AbstractFilter {
    public static final String TYPE =  "combine";

    private String operator;
    List<IFilter> innerFilters;

    public CombineFilter() {
        super(TYPE);
    }

    public CombineFilter(String operator) {
        super(TYPE);
        this.operator = operator;
    }

    public CombineFilter(String operator, List<IFilter> innerFilters) {
        super(TYPE);
        this.operator = operator;
        this.innerFilters = innerFilters;
    }

    public CombineFilter(String operator, List<IFilter> innerFilters, boolean positive) {
        super(TYPE, positive);
        this.operator = operator;
        this.innerFilters = innerFilters;
    }

    public static class Ops {
        public static final String And = "and";
        public static final String Or = "or";
    }

    public static IFilter create(String operator, List<IFilter> innerFilters) {
        if (innerFilters == null) {
            return null;
        }
        innerFilters.removeIf(Objects::isNull);
        if (innerFilters.isEmpty()) {
            return null;
        }

        if (innerFilters.size() == 1) {
            return innerFilters.get(0);
        }

        return new CombineFilter(operator, innerFilters);
    }

}
