package com.happy3w.persistence.core.rowdata.column;

import com.happy3w.persistence.core.rowdata.IColumnMatcher;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.persistence.core.rowdata.simple.RdColumnDef;

import java.util.Map;

public class DynamicColumnMatcher implements IColumnMatcher {
    private final IRdTableDef rdTableDef;
    private final Map<String, ColumnInfo> titleToColumnInfo;

    public DynamicColumnMatcher(IRdTableDef rdTableDef) {
        this.rdTableDef = rdTableDef;
        titleToColumnInfo = ColumnInfo.createInfoMap(rdTableDef.getColumns());
    }

    @Override
    public ColumnInfo genColumnInfo(String columnTitle, int pageColumnIndex) {
        ColumnInfo columnInfo = titleToColumnInfo.get(columnTitle);
        if (columnInfo != null) {
            columnInfo.setPageColumnIndex(pageColumnIndex);
            titleToColumnInfo.remove(columnTitle);
        } else {
            RdColumnDef newColumn = new RdColumnDef(columnTitle, columnTitle, String.class, false);
            columnInfo = new ColumnInfo(pageColumnIndex, rdTableDef.getColumns().size(), newColumn);
            rdTableDef.getColumns().add(newColumn);
            titleToColumnInfo.put(columnTitle, columnInfo);
        }
        return columnInfo;
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
