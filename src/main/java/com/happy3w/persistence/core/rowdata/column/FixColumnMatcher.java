package com.happy3w.persistence.core.rowdata.column;

import com.happy3w.persistence.core.rowdata.IColumnMatcher;
import com.happy3w.persistence.core.rowdata.IRdTableDef;

import java.util.Map;

public class FixColumnMatcher implements IColumnMatcher {
    private final IRdTableDef rdTableDef;
    private final Map<String, ColumnInfo> titleToColumnInfo;

    public FixColumnMatcher(IRdTableDef rdTableDef) {
        this.rdTableDef = rdTableDef;
        titleToColumnInfo = ColumnInfo.createInfoMap(rdTableDef.getColumns());
    }

    @Override
    public ColumnInfo genColumnInfo(String columnTitle, int pageColumnIndex) {
        ColumnInfo columnInfo = titleToColumnInfo.get(columnTitle);
        if (columnInfo != null) {
            columnInfo.setPageColumnIndex(pageColumnIndex);
            titleToColumnInfo.remove(columnTitle);
        }
        return columnInfo;
    }

    @Override
    public boolean isFinished() {
        return titleToColumnInfo.isEmpty();
    }
}
