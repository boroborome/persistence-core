package com.happy3w.persistence.core.rowdata;

import java.util.List;

public interface IRdTableDef<ColumnType extends IRdColumnDef> extends IExtConfigs {
    List<ColumnType> getColumns();
}
