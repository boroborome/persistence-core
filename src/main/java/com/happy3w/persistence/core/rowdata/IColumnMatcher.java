package com.happy3w.persistence.core.rowdata;

import com.happy3w.persistence.core.rowdata.column.ColumnInfo;

public interface IColumnMatcher {

    ColumnInfo genColumnInfo(String columnTitle, int pageColumnIndex);

    boolean isFinished();
}
