package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.toolkits.convert.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class MemReadDataPage implements IReadDataPage<MemReadDataPage> {

    private String pageName;

    private List<Object[]> rows = new ArrayList<>();

    private TypeConverter typeConverter = TypeConverter.INSTANCE;

    @Override
    public <D> D readValue(int rowIndex, int columnIndex, Class<D> dataType, ExtConfigs extConfigs) {
        if (rowIndex < 0 || rowIndex >= rows.size()) {
            return null;
        }
        Object[] rowDatas = rows.get(rowIndex);
        if (rowDatas == null || columnIndex < 0 || columnIndex >= rowDatas.length) {
            return null;
        }
        return typeConverter.convert(rowDatas[columnIndex], dataType);
    }

    public MemReadDataPage pageName(String pageName) {
        this.pageName = pageName;
        return this;
    }

    public MemReadDataPage typeConverter(TypeConverter typeConverter) {
        this.typeConverter = typeConverter;
        return this;
    }

    public MemReadDataPage rowData(Object... rowDatas) {
        rows.add(rowDatas);
        return this;
    }

    @Override
    public String getPageName() {
        return pageName;
    }
}
