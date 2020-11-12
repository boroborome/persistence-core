package com.happy3w.persistence.core.filter;

import lombok.Getter;

@Getter
public abstract class AbstractSingleFieldFilter implements IFieldFilter {
    protected String type;
    protected boolean positive = true;
    protected String field;

    public AbstractSingleFieldFilter(String type) {
        this.type = type;
    }
    public AbstractSingleFieldFilter(String type, String field) {
        this.type = type;
        this.field = field;
    }
}
