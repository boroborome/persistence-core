package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.IExtConfigs;

public interface IDataPage<T extends IDataPage<T>> extends IExtConfigs {
    String getPageName();

    T locate(int row, int column);
}
