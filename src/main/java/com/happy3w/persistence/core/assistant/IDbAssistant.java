package com.happy3w.persistence.core.assistant;

import com.happy3w.persistence.core.filter.IFilter;

import java.util.List;
import java.util.stream.Stream;

public interface IDbAssistant {
    <T> void saveData(T data);
    <T> void saveStream(Stream<T> dataStream);
    <T> Stream<T> queryStream(Class<T> dataType, List<IFilter> filters, QueryOptions options);
}
