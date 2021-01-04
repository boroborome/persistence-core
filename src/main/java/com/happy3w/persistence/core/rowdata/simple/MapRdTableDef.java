package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.toolkits.message.MessageRecorder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRdTableDef extends AbstractRdTableDef<Map<String, Object>, RdColumnDef, MapRdTableDef> {
    public MapRdTableDef() {
        columns = new ArrayList<>();
    }

    @Override
    public List<Object> toColumnValues(Map<String, Object> data) {
        List<Object> rows = new ArrayList<>();
        for (RdColumnDef columnDef : columns) {
            Object rowValue = data.get(columnDef.getCode());
            rows.add(rowValue);
        }
        return rows;
    }

    @Override
    public RdRowWrapper<Map<String, Object>> toRowData(RdRowWrapper<List<Object>> columnValuesWrapper, MessageRecorder messageRecorder) {
        List<Object> listValues = columnValuesWrapper.getData();

        Map<String, Object> mapValues = new HashMap<>();
        for (int columnIndex = columns.size() - 1; columnIndex >= 0; columnIndex--) {
            RdColumnDef column = columns.get(columnIndex);
            mapValues.put(column.getCode(), listValues.get(columnIndex));
        }
        return columnValuesWrapper.withNewData(mapValues);
    }
}
