package com.happy3w.persistence.core.filter;

import lombok.Getter;

@Getter
public abstract class AbstractFilter implements IFilter {
    protected String type;
    protected boolean positive = true;

    public AbstractFilter(String type) {
        this.type = type;
    }
    public AbstractFilter(String type, boolean positive) {
        this.type = type;
        this.positive = positive;
    }
}
