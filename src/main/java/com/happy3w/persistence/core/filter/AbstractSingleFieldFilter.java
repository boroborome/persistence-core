package com.happy3w.persistence.core.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractSingleFieldFilter extends AbstractFieldFilter implements IFieldFilter {
    protected String field;

    public AbstractSingleFieldFilter(String type) {
        super(type);
    }

    public AbstractSingleFieldFilter(String type, String field) {
        super(type);
        this.field = field;
    }

    public AbstractSingleFieldFilter(String type, String field, boolean positive) {
        super(type);
        this.field = field;
        this.positive = positive;
    }
}
