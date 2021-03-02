package com.happy3w.persistence.core.assistant;

import com.happy3w.persistence.core.filter.IFilter;

import java.util.List;
import java.util.stream.Stream;

/**
 * 访问数据库的助手。
 * 目标是完成任意类型数据的读写查。主要特点：单个数据类型的批量操作
 */
public interface IDbAssistant<KT> {
    <T> T saveData(T data);
    <T> void saveStream(Stream<T> dataStream);
    <T> T findById(Class<T> dataType, KT id);
    <T> Stream<T> findByFilter(Class<T> dataType, List<? extends IFilter> filters, QueryOptions options);
    <T> T deleteById(Class<T> dataType, KT id);
    <T> long deleteByFilter(Class<T> dataType, List<? extends IFilter> filters, QueryOptions options);
}
