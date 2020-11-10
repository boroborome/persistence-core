package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;

/**
 * 读文件接口
 * @param <T> 自身类型
 */
public interface IReadDataPage<T extends IReadDataPage<T>> extends IDataPage<T> {

    /**
     * 读取指定单元格数据
     * @param rowIndex 行索引
     * @param columnIndex 列索引
     * @param dataType 期望这个单元格的数据类型。返回类型应该和这个类型一致
     * @param extConfigs 这个单元格上的配置
     * @param <D> 返回数据类型
     * @return 返回读取到的数据
     */
    <D> D readValue(int rowIndex, int columnIndex, Class<D> dataType, ExtConfigs extConfigs);
}
