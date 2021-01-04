package com.happy3w.persistence.core.rowdata.column;

import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.toolkits.message.MessageRecorder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Setter
@AllArgsConstructor
public class ColumnInfo {
    private int pageColumnIndex;
    private int tableColumnIndex;
    private IRdColumnDef columnDef;

    public static Map<String, ColumnInfo> createInfoMap(List<? extends IRdColumnDef> columns) {
        Map<String, ColumnInfo> map = new HashMap<>();
        if (columns != null) {
            for (int tableColumnIndex = 0; tableColumnIndex < columns.size(); tableColumnIndex++) {
                IRdColumnDef column = columns.get(tableColumnIndex);
                ColumnInfo info = new ColumnInfo(-1, tableColumnIndex, column);
                map.put(column.getTitle(), info);
            }
        }
        return map;
    }


    public Object readValue(int row, IReadDataPage<?> dataPage, MessageRecorder messageRecorder) {
        try {
            return dataPage.readValue(row, pageColumnIndex,
                    columnDef.getDataType(),
                    columnDef.getExtConfigs());
        } catch (Exception e) {
            String msg = e.getMessage();
            if (msg == null) {
                msg = "Empty Message.";
            }
            log.error(msg, e);

            messageRecorder.appendError(msg);
            return null;
        }
    }
}
