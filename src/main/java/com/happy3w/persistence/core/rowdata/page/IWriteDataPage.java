package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;

/**
 * 写数据接口
 * @param <T> 自身类型
 */
public interface IWriteDataPage<T extends IWriteDataPage<T>> extends IDataPage<T> {

    /**
     * 换行。表示结束当前数据
     * @return 返回自己。这是为了方便操作
     */
    T newLine();

    /**
     * 写一个数据到当前位置，写完后向后移动一列。
     * @param value 需要写入的值
     * @param configs 写入数据使用的配置
     * @return 返回自己。这是为了方便操作
     */
    T writeValueCfg(Object value, ExtConfigs configs);
}
