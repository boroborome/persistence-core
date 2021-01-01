package com.happy3w.persistence.core.filter.impl;

import com.happy3w.persistence.core.filter.AbstractSingleFieldFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public abstract class AbstractInFilter<DT> extends AbstractSingleFieldFilter {
    protected Collection<DT> refs;

    public AbstractInFilter(String type, String field, Collection<DT> refs) {
        this(type, field, refs, true);
    }

    public AbstractInFilter(String type, String field, Collection<DT> refs, boolean isPositive) {
        super(type);
        this.field = field;
        this.refs = refs;
        this.positive = isPositive;
    }
}
