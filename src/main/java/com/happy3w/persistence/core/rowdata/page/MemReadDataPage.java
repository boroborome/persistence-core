package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.toolkits.convert.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class MemReadDataPage implements IReadDataPage<MemReadDataPage> {

    private ExtConfigs extConfigs;

    private String pageName;

    private List<Object[]> rows = new ArrayList<>();
    private int row;
    private int column;

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

    public MemReadDataPage extConfig(ExtConfigs extConfigs) {
        this.extConfigs = extConfigs;
        return this;
    }

    @Override
    public String getPageName() {
        return pageName;
    }

    @Override
    public MemReadDataPage locate(int row, int column) {
        this.row = row;
        this.column = column;
        return this;
    }

    @Override
    public ExtConfigs getExtConfigs() {
        return extConfigs;
    }

    @Override
    public void setExtConfigs(ExtConfigs extConfigs) {
        this.extConfigs = extConfigs;
    }
}
