package com.happy3w.persistence.core.filter;

import lombok.Getter;

@Getter
public abstract class AbstractFieldFilter extends AbstractFilter implements IFieldFilter {
    public AbstractFieldFilter(String type) {
        super(type);
    }

    public AbstractFieldFilter(String type, boolean positive) {
        super(type, positive);
    }
}
