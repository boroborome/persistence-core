package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;

public interface IReadDataPage<T extends IReadDataPage<T>> extends IDataPage<T> {

    <D> D readValue(int rowIndex, int columnIndex, Class<D> dataType, ExtConfigs extConfigs);
}
