package com.happy3w.persistence.core.rowdata;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.persistence.core.rowdata.page.IWriteDataPage;
import com.happy3w.toolkits.message.MessageRecorder;

import java.util.List;
import java.util.stream.Stream;

/**
 * 行数据助手，提供从page读写行数据的常用方法<br>
 *     方法命名规则如下:{Action}{DataType}{Tag}<br>
 *     Action可以选
 *     <ul>
 *         <li>read: 从page读取数据操作</li>
 *         <li>write: 将数据写入page功能</li>
 *     </ul>
 *     DataType可以选择
 *     <ul>
 *         <li>Value: 表示操作数据仅仅是一个简单数据，需要写入一个单元格中。此时如果传入的需要写入的数据为List，则将List中内容拼接为一个字符串输出</li>
 *         <li>List:表示操作数据为列表，方法会遍历列表中每个值，执行对应writeValueXXX方法</li>
 *         <li>Obj:表示操作数据为代表这一行数据的完整对象，需要根据对象定义从对象上获取需要输出的属性，这个输出</li>
 *         <li>Row:表示操作的数据为代表行数据的一个Wrapper</li>
 *     </ul>
 *     Tag可以选择
 *     <ul>
 *         <li>Cfg:表示需要通过特定配置输出</li>
 *     </ul>
 */
public class RdAssistant {
    /**
     * 从当前位置读取数据。当前位置包含Title
     * @param tableDef 行数据定义
     * @param page 包含需要读取数据的页面
     * @param messageRecorder 消息记录器
     * @param <D> 行数据的类型
     * @param <P> 包含需要读取数据的页面类型
     * @return 以流的形式返回所有行数据
     */
    public static <D, P extends IReadDataPage> Stream<RdRowWrapper<D>> readRows(
            IRdTableDef<D, ?> tableDef, P page, MessageRecorder messageRecorder) {
        return RdRowIterator.from(page, tableDef, messageRecorder)
                .stream();
    }

    public static <D, P extends IReadDataPage> Stream<D> readObjs(
            P page, IRdTableDef<D, ?> tableDef, MessageRecorder messageRecorder) {
        return readRows(tableDef, page, messageRecorder)
                .map(RdRowWrapper::getData);
    }

    public static <P extends IWriteDataPage> void writeValue(P page, Object... values) {
        for (Object v : values) {
            page.writeValueCfg(v, null);
        }
    }

    public static <P extends IWriteDataPage> void writeList(List values, P page) {
        for (Object v : values) {
            page.writeValueCfg(v, null);
        }
    }

    public static <P extends IWriteDataPage> void writeList(List rowValue, P page, IRdTableDef tableDef) {
        List<IRdColumnDef> columns = tableDef.getColumns();
        int columnSize = columns.size();
        if (columnSize != rowValue.size()) {
            throw new IllegalArgumentException("Row values count is not match table column count.");
        }

        for (int columnIndex = 0; columnIndex < columnSize; columnIndex++) {
            IRdColumnDef columnDef = columns.get(columnIndex);
            page.writeValueCfg(rowValue.get(columnIndex), columnDef.getExtConfigs());
        }
    }

    public static <D, P extends IWriteDataPage> void writeObj(Stream<D> datas, P page, IRdTableDef<D, ?> tableDef) {
        ExtConfigs orgConfigs = page.getExtConfigs();
        try {
            ExtConfigs configs = new ExtConfigs();
            configs.merge(orgConfigs);
            configs.merge(tableDef.getExtConfigs());
            page.setExtConfigs(configs);

            List<String> titles = ListUtils.map(tableDef.getColumns(), c -> c.getTitle());
            writeList(titles, page);
            page.newLine();
            datas.forEach(data -> {
                List<Object> values = tableDef.toColumnValues(data);
                writeList(values, page, tableDef);
                page.newLine();
            });
        } finally {
            page.setExtConfigs(orgConfigs);
        }
    }
}
