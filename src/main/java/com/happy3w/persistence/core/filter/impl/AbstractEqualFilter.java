package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractEqualFilter<DT> extends AbstractSingleFieldFilter {
    protected DT ref;

    public AbstractEqualFilter(String type) {
        super(type);
    }
    public AbstractEqualFilter(String type, String field, DT ref) {
        this(type, field, ref, true);
    }

    public AbstractEqualFilter(String type, String field, DT ref, boolean isPositive) {
        super(type, field, isPositive);
        this.ref = ref;
    }
}
