package com.happy3w.persistence.core.filter;

import java.util.List;

public interface IFilterAble {
    default List<IFilter> toFilterList() {
        return FilterAssistant.createFitler(this);
    }
}
