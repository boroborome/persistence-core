package com.happy3w.persistence.core.rowdata;

import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.toolkits.iterator.NeedFindIterator;
import com.happy3w.toolkits.message.MessageFilter;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    protected void findNext() {
        if (columnInfoList == null) {
            initialize(currentRow, 0);
        }

        nextItem = loadData(currentRow);
        currentRow++;

        status = nextItem == null ? IteratorStatus.end : IteratorStatus.found;
    }

    public int loadSchema(int rowIndex, int startColumn) {
        columnInfoList = new ArrayList<>();

        Map<String, ColumnInfo> titleToColumnInfo = ColumnInfo.createInfoMap(tableDef.getColumns());

        for (int pageColumnIndex = startColumn; ; ++pageColumnIndex) {
            String columnTitle = page.readValue(rowIndex, pageColumnIndex, String.class, null);
            if (columnTitle != null) {
                columnTitle = columnTitle.trim();
            }
            if (StringUtils.isEmpty(columnTitle)) {
                break;
            }

            ColumnInfo columnInfo = titleToColumnInfo.get(columnTitle);
            if (columnInfo == null) {
                if (tableDef.getUnknownColumnStrategy() == UnknownColumnStrategy.error) {
                    messageRecorder.appendError("Unexpected column:{0}", columnTitle);
                }
                continue;
            }

            columnInfo.pageColumnIndex = pageColumnIndex;
            columnInfoList.add(columnInfo);
            titleToColumnInfo.remove(columnTitle);
            if (titleToColumnInfo.isEmpty()) {
                break;
            }
        }

        checkAllRequiredFieldMustExist(titleToColumnInfo.values());

        return rowIndex + 1;
    }

    private void checkAllRequiredFieldMustExist(Collection<ColumnInfo> leftColumnInfos) {
        List<String> lostRequiredColumns = new ArrayList<>();
        for (ColumnInfo columnInfo : leftColumnInfos) {
            IRdColumnDef columnDef = columnInfo.columnDef;
            if (columnDef.isRequired()) {
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
            columnValues.set(columnInfo.tableColumnIndex, columnValue);
        }

        if (columnCountWithValue == 0) {
            return null;
        }

        RdRowWrapper<List<Object>> columnValuesWrapper = new RdRowWrapper<>(columnValues, page.getPageName(), currentRow + 1);
        RdRowWrapper<T> rowDataWrapper = tableDef.toRowData(columnValuesWrapper, messageRecorder);
        rowDataWrapper.setError(errorRecorder.getMessage());
        return rowDataWrapper;
    }

    public Stream<RdRowWrapper<T>> stream() {
        Iterable<RdRowWrapper<T>> iterable = () -> this;
        return StreamSupport.stream(iterable.spliterator(), false);
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

    @Getter
    @AllArgsConstructor
    private static class ColumnInfo {
        private int pageColumnIndex;
        private int tableColumnIndex;
        private IRdColumnDef columnDef;

        public static Map<String, ColumnInfo> createInfoMap(List<? extends IRdColumnDef> columns) {
            Map<String, ColumnInfo> map = new HashMap<>();
            for (int tableColumnIndex = 0; tableColumnIndex < columns.size(); tableColumnIndex++) {
                IRdColumnDef column = columns.get(tableColumnIndex);
                ColumnInfo info = new ColumnInfo(-1, tableColumnIndex, column);
                map.put(column.getTitle(), info);
            }
            return map;
        }


        public Object readValue(int row, IReadDataPage<?> dataPage, MessageRecorder messageRecorder) {
            try {
                return dataPage.readValue(row, pageColumnIndex,
                        columnDef.getDataType(),
                        columnDef.getExtConfigs());
            } catch (Exception e) {
                messageRecorder.appendError(e.getMessage());
                return null;
            }
        }
    }
}