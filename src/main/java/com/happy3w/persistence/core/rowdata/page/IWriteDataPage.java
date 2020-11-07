package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.toolkits.utils.ListUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * 可以写的Page<br>
 *     write方法命名规则如下:write{DataType}{Tag}<br>
 *     DataType可以选择
 *     <ul>
 *         <li>Value: 表示输入的参数仅仅是一个简单数据，需要写入一个单元格中。此时如果传入的需要写入的数据为List，则将List中内容拼接为一个字符串输出</li>
 *         <li>List:表示输入的参数为列表，方法会遍历列表中每个值，执行对应writeValueXXX方法</li>
 *         <li>Obj:表示输入的参数为完整对象，需要根据对象定义从对象上获取需要输出的属性，这个输出</li>
 *         <li>Stream:表示输入的参数为流。</li>
 *     </ul>
 *     Tag可以选择
 *     <ul>
 *         <li>Cfg:表示需要通过特定配置输出</li>
 *     </ul>
 * @param <T> 当前类的类型
 */
public interface IWriteDataPage<T extends IWriteDataPage<T>> extends IDataPage<T> {
    T newLine();

    T writeValueCfg(Object value, ExtConfigs configs);

    default T writeValue(Object value) {
        return writeValueCfg(value, null);
    }

    default T writeValue(Object... values) {
        for (Object v : values) {
            writeValueCfg(v, null);
        }
        return (T) this;
    }

    default T writeList(List values) {
        for (Object v : values) {
            writeValueCfg(v, null);
        }
        return (T) this;
    }

    default T writeList(List rowValue, IRdTableDef tableDef) {
        List<IRdColumnDef> columns = tableDef.getColumns();
        int columnSize = columns.size();
        if (columnSize != rowValue.size()) {
            throw new IllegalArgumentException("Row values count is not match table column count.");
        }

        for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
            IRdColumnDef columnDef = columns.get(columnIndex);
            writeValueCfg(rowValue.get(columnIndex), columnDef.getExtConfigs());
        }

        return (T) this;
    }

    default <D> T writeStream(Stream<D> datas, IRdTableDef<D, ?> tableDef) {
        ExtConfigs orgConfigs = getExtConfigs();
        try {
            ExtConfigs configs = new ExtConfigs();
            configs.merge(orgConfigs);
            configs.merge(tableDef.getExtConfigs());
            setExtConfigs(configs);

            List<String> titles = ListUtils.map(tableDef.getColumns(), c -> c.getTitle());
            writeList(titles)
                    .newLine();
            datas.forEach(data -> {
                List<Object> values = tableDef.toColumnValues(data);
                writeList(values, tableDef);
                newLine();
            });
        } finally {
            setExtConfigs(orgConfigs);
        }
        return (T) this;
    }
}
