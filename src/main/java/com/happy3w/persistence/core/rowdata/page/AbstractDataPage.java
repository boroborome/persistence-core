package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractDataPage<T extends AbstractDataPage<T>> implements IDataPage<T> {
    @Getter
    @Setter
    protected ExtConfigs extConfigs = new ExtConfigs();

    protected int row;
    protected int column;
}
