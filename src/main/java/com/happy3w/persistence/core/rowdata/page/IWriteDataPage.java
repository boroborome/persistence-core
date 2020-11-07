package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;

public interface IWriteDataPage<T extends IWriteDataPage<T>> extends IDataPage<T> {
    T newLine();

    T writeValueCfg(Object value, ExtConfigs configs);
}
