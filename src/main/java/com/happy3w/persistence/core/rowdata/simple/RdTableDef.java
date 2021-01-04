package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.toolkits.message.MessageRecorder;

import java.util.ArrayList;
import java.util.List;

public class RdTableDef extends AbstractRdTableDef<List<Object>, RdColumnDef, RdTableDef> {
    public RdTableDef() {
        columns = new ArrayList<>();
    }

    @Override
    public List<Object> toColumnValues(List<Object> data) {
        return data;
    }

    @Override
    public RdRowWrapper<List<Object>> toRowData(RdRowWrapper<List<Object>> columnValuesWrapper, MessageRecorder messageRecorder) {
        return columnValuesWrapper;
    }
}
