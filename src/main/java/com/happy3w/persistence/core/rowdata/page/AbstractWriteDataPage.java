package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractWriteDataPage<T extends AbstractWriteDataPage<T>> extends AbstractDataPage<T> implements IWriteDataPage<T> {
    @Getter
    @Setter
    protected ExtConfigs extConfigs = new ExtConfigs();

    @SuppressWarnings("unchecked")
    @Override
    public T newLine() {
        row++;
        column = 0;
        return (T) this;
    }
}
