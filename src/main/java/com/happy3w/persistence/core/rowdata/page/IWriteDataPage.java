package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.persistence.core.rowdata.config.ObjRdConfigImpl;
import com.happy3w.persistence.core.rowdata.obj.ObjRdColumnDef;
import com.happy3w.persistence.core.rowdata.obj.ObjRdTableDef;
import com.happy3w.toolkits.utils.ListUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * 可以写的Page<br>
 * @param <T> 当前类的类型
 */
public interface IWriteDataPage<T extends IWriteDataPage<T>> extends IDataPage<T> {
    T newLine();

    T writeObjWithConfig(Object value, ExtConfigs extConfigs);

    default T writeList(List values) {
        for (Object v : values) {
            writeObjWithConfig(v, null);
        }
        return (T) this;
    }

    default T writeValue(Object... values) {
        return writeList(Arrays.asList(values));
    }

    default T writeListLine(List values) {
        return writeList(values)
                .newLine();
    }

    default T writeValueLine(Object... values) {
        return writeListLine(Arrays.asList(values));
    }

    default T writeList(List rowValue, IRdTableDef tableDef) {
        List<IRdColumnDef> columns = tableDef.getColumns();
        int columnSize = columns.size();
        if (columnSize != rowValue.size()) {
            throw new IllegalArgumentException("Row values count is not match table column count.");
        }

        for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
            IRdColumnDef columnDef = columns.get(columnIndex);
            writeObjWithConfig(rowValue.get(columnIndex), columnDef.getExtConfigs());
        }

        return (T) this;
    }

    default <D> T writeObj(Stream<D> datas, Class<D> dataType) {
        ObjRdTableDef<D> objectDef = ObjRdTableDef.from(dataType);

        ExtConfigs originExtendInfo = getExtConfigs();
        try {
            ExtConfigs extend = new ExtConfigs();
            extend.merge(originExtendInfo);
            extend.merge(objectDef.getExtConfigs());
            setExtConfigs(extend);

            List<String> titles = ListUtils.map(objectDef.getColumns(), c -> c.getTitle());
            writeList(titles)
                    .newLine();
            datas.forEach(data -> {
                for (ObjRdColumnDef columnDefinition : objectDef.getColumns()) {
                    Object value = columnDefinition.getAccessor().getValue(data);
                    writeObjWithConfig(value, columnDefinition.getExtConfigs());
                }
                newLine();
            });
        } finally {
            setExtConfigs(originExtendInfo);
        }
        return (T) this;
    }
}
