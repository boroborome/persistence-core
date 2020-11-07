package com.happy3w.persistence.core.rowdata;

import com.happy3w.toolkits.message.MessageRecorder;

import java.util.List;

public interface IRdTableDef<RowData, ColumnType extends IRdColumnDef> extends IExtConfigs {
    List<ColumnType> getColumns();
    List<Object> toColumnValues(RowData data);
    RdRowWrapper<RowData> toRowData(RdRowWrapper<List<Object>> columnValuesWrapper, MessageRecorder messageRecorder);

    UnknownColumnStrategy getUnknownColumnStrategy();
}
