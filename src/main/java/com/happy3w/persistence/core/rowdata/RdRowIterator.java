package com.happy3w.persistence.core.rowdata;

import com.happy3w.persistence.core.rowdata.column.ColumnInfo;
import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.toolkits.iterator.NeedFindIterator;
import com.happy3w.toolkits.iterator.NullableOptional;
import com.happy3w.toolkits.message.MessageFilter;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class RdRowIterator<T> extends NeedFindIterator<RdRowWrapper<T>> {
    private final IReadDataPage<?> page;
    private IRdTableDef<T, ? extends IRdColumnDef> tableDef;
    protected MessageRecorder messageRecorder;

    protected List<ColumnInfo> columnInfoList;

    protected int currentRow;

    public RdRowIterator(IReadDataPage<?> page, IRdTableDef<T, ? extends IRdColumnDef> tableDef, MessageRecorder messageRecorder) {
        this.page = page;
        this.tableDef = tableDef;
        this.messageRecorder = messageRecorder;
    }

    public MessageRecorder getMessageRecorder() {
        return messageRecorder;
    }

    public final void initialize(int rowIndex, int startColumn) {
        currentRow = loadSchema(rowIndex, startColumn);
    }

    @Override
    protected NullableOptional<RdRowWrapper<T>> findNext() {
        if (columnInfoList == null) {
            initialize(currentRow, 0);
        }

        RdRowWrapper<T> nextItem = loadData(currentRow);
        currentRow++;

        return nextItem == null ? NullableOptional.empty() : NullableOptional.of(nextItem);
    }

    public int loadSchema(int rowIndex, int startColumn) {
        columnInfoList = new ArrayList<>();

        IColumnMatcher columnMatcher = tableDef.createColumnMatcher();

        for (int pageColumnIndex = startColumn; ; ++pageColumnIndex) {
            String columnTitle = page.readValue(rowIndex, pageColumnIndex, String.class, null);
            if (columnTitle != null) {
                columnTitle = columnTitle.trim();
            }
            if (StringUtils.isEmpty(columnTitle)) {
                break;
            }

            ColumnInfo columnInfo = columnMatcher.genColumnInfo(columnTitle, pageColumnIndex);
            if (columnInfo == null) {
                if (tableDef.getUnknownColumnStrategy() == UnknownColumnStrategy.error) {
                    messageRecorder.appendError("Unexpected column:{0}", columnTitle);
                }
                continue;
            }

            columnInfoList.add(columnInfo);
            if (columnMatcher.isFinished()) {
                break;
            }
        }

        checkAllRequiredFieldMustExist();

        return rowIndex + 1;
    }

    private void checkAllRequiredFieldMustExist() {
        List<String> lostRequiredColumns = new ArrayList<>();
        Map<String, ColumnInfo> existColumns = ListUtils.toMap(columnInfoList, c -> c.getColumnDef().getTitle());
        for (IRdColumnDef columnDef : tableDef.getColumns()) {
            if (columnDef.isRequired() && !existColumns.containsKey(columnDef.getTitle())) {
                lostRequiredColumns.add(columnDef.getTitle());
            }
        }
        if (!lostRequiredColumns.isEmpty()) {
            messageRecorder.appendError(
                    "{0} is not a validate page, These required column are lost:{1}.",
                    page.getPageName(), lostRequiredColumns.toString());
        }
    }

    public RdRowWrapper<T> loadData(int currentRow) {
        if (columnInfoList.isEmpty()) {
            return null;
        }

        List<Object> columnValues = ListUtils.newList(tableDef.getColumns().size(), null);
        MessageFilter errorRecorder = messageRecorder.startErrorFilter();

        int columnCountWithValue = 0;
        for (ColumnInfo columnInfo : columnInfoList) {
            Object columnValue = columnInfo.readValue(currentRow, page, messageRecorder);
            if (columnValue == null) {
                continue;
            }
            columnCountWithValue++;
            columnValues.set(columnInfo.getTableColumnIndex(), columnValue);
        }

        if (columnCountWithValue == 0) {
            return null;
        }

        RdRowWrapper<List<Object>> columnValuesWrapper = new RdRowWrapper<>(columnValues, page.getPageName(), currentRow + 1);
        RdRowWrapper<T> rowDataWrapper = tableDef.toRowData(columnValuesWrapper, messageRecorder);
        rowDataWrapper.setError(errorRecorder.getMessage());
        return rowDataWrapper;
    }

    public static <T> RdRowIterator<T> from(
            IReadDataPage dataPage,
            IRdTableDef<T, ? extends IRdColumnDef> tableDef) {
        return from(dataPage, tableDef, new MessageRecorder());
    }

    public static <T> RdRowIterator<T> from(
            IReadDataPage dataPage,
            IRdTableDef<T, ? extends IRdColumnDef> tableDef,
            MessageRecorder messageRecorder) {
        RdRowIterator<T> iterator = new RdRowIterator<>(dataPage, tableDef, messageRecorder);
        iterator.initialize(0, 0);
        return iterator;
    }

}
