package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.toolkits.iterator.NeedFindIterator;
import com.happy3w.toolkits.message.MessageFilter;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ObjRdRowIterator<T> extends NeedFindIterator<ObjRdRowWrapper<T>> {
    private final IReadDataPage<?> page;
    private ObjRdTableDef<T> objDef;
    protected MessageRecorder messageRecorder;

    protected List<FieldInfo> fieldInfoList;

    protected int currentRow;

    public ObjRdRowIterator(IReadDataPage page, ObjRdTableDef<T> objDef, MessageRecorder messageRecorder) {
        this.page = page;
        this.objDef = objDef;
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
        if (fieldInfoList == null) {
            initialize(currentRow, 0);
        }

        nextItem = loadData(currentRow);
        currentRow++;

        status = nextItem == null ? IteratorStatus.end : IteratorStatus.found;
    }

    public int loadSchema(int rowIndex, int startColumn) {
        fieldInfoList = new ArrayList();
        Map<String, ObjRdColumnDef> fieldMap = ListUtils.toMap(objDef.getColumns(), ObjRdColumnDef::getTitle);

        for (int index = startColumn; ; ++index) {
            String columnValue = page.readValue(rowIndex, index, String.class, null);
            if (StringUtils.isEmpty(columnValue)) {
                break;
            }
            String columnTitle = columnValue.trim();
            ObjRdColumnDef columnDefinition = fieldMap.get(columnTitle);
            if (columnDefinition == null) {
                if (objDef.getUnexpectedColumnStrategy() == ObjRdUnknownColumn.Strategy.error) {
                    messageRecorder.appendError("Unexpected column:{0}", columnTitle);
                }
                continue;
            }

            FieldInfo fieldInfo = new FieldInfo(index, columnDefinition);
            fieldInfoList.add(fieldInfo);

            if (fieldInfoList.size() == fieldMap.size()) {
                break;
            }
        }

        checkAllRequiredFieldMustExist(objDef, fieldInfoList);

        return rowIndex + 1;
    }

    private void checkAllRequiredFieldMustExist(ObjRdTableDef<T> objectDefinition, List<FieldInfo> modelInformation) {
        Map<String, FieldInfo> fieldInfoMap = ListUtils.toMap(modelInformation,
                f -> f.getColumnDefinition().getTitle());

        List<String> lostRequiredField = new ArrayList<>();
        for (ObjRdColumnDef columnDefinition : objectDefinition.getColumns()) {
            if (columnDefinition.isRequired() && !fieldInfoMap.containsKey(columnDefinition.getTitle())) {
                lostRequiredField.add(columnDefinition.getTitle());
            }
        }
        if (!lostRequiredField.isEmpty()) {
            messageRecorder.appendError(
                    "{0} is not a validate page, These required column are lost:{1}.",
                    page.getPageName(), lostRequiredField.toString());
        }
    }

    public ObjRdRowWrapper<T> loadData(int currentRow) {
        if (fieldInfoList.isEmpty()) {
            return null;
        }

        try {
            T data = objDef.getDataType().newInstance();
            ObjRdRowWrapper<T> wrapper = new ObjRdRowWrapper<T>(data, page.getPageName(), currentRow + 1);
            MessageFilter errorRecorder = messageRecorder.startErrorFilter();

            int fieldCountWithValue = 0;
            for (FieldInfo fieldInfo : fieldInfoList) {
                Object fieldValue = fieldInfo.readValue(currentRow, page, messageRecorder);
                if (fieldValue == null) {
                    continue;
                }
                fieldCountWithValue++;
                fieldInfo.validateValue(fieldValue, wrapper, messageRecorder);
                fieldInfo.saveField(fieldValue, data);
            }

            if (fieldCountWithValue == 0) {
                return null;
            }

            objDef.runPostAction(wrapper, messageRecorder);
            wrapper.setError(errorRecorder.getMessage());
            return wrapper;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UnsupportedOperationException("Failed to create instance " + objDef.getDataType(), e);
        }
    }

    public Stream<ObjRdRowWrapper<T>> stream() {
        Iterable<ObjRdRowWrapper<T>> iterable = () -> this;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    public static <T> ObjRdRowIterator<T> from(IReadDataPage dataPage, Class<T> dataType) {
        return from(dataPage, ObjRdTableDef.from(dataType), new MessageRecorder());
    }

    public static <T> ObjRdRowIterator<T> from(IReadDataPage dataPage, Class<T> dataType, MessageRecorder messageRecorder) {
        return from(dataPage, ObjRdTableDef.from(dataType), messageRecorder);
    }

    public static <T> ObjRdRowIterator<T> from(
            IReadDataPage dataPage,
            ObjRdTableDef<T> objectDefinition,
            MessageRecorder messageRecorder) {
        ObjRdRowIterator<T> iterator = new ObjRdRowIterator<>(dataPage, objectDefinition, messageRecorder);
        iterator.initialize(0, 0);
        return iterator;
    }

    @Getter
    @AllArgsConstructor
    private static class FieldInfo {
        private int columnIndex;
        private ObjRdColumnDef columnDefinition;


        public Object readValue(int row, IReadDataPage<?> dataPage, MessageRecorder messageRecorder) {
            try {
                return dataPage.readValue(row, columnIndex,
                        columnDefinition.getAccessor().getDataType(),
                        columnDefinition.getExtConfigs());
            } catch (Exception e) {
                messageRecorder.appendError(e.getMessage());
                return null;
            }
        }

        public void validateValue(Object curValue, ObjRdRowWrapper<?> wrapper, MessageRecorder messageRecorder) {
            ObjRdValueValidator validator = columnDefinition.getValidator();
            if (validator != null) {
                validator.validate(curValue, columnDefinition, wrapper, messageRecorder);
            }
        }

        public void saveField(Object curValue, Object data) {
            try {
                columnDefinition.getAccessor().getSetMethod().invoke(data, curValue);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new UnsupportedOperationException("Failed to write field:" + columnDefinition.getAccessor().getFieldName(), e);
            }
        }
    }
}
