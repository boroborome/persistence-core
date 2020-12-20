package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IExtConfigs;

public interface IDataPage<T extends IDataPage<T>> extends IExtConfigs {
    default T extConfigs(ExtConfigs extConfigs) {
        setExtConfigs(extConfigs);
        return (T) this;
    }
}
