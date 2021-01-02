package com.happy3w.persistence.core.filter;

import com.happy3w.toolkits.reflect.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilterAssistant {

    private static Map<Class, List<FieldInfo>> fieldInfoMap = new HashMap<>();
    private static Map<Class, IFilterProcessor> processorMap = new HashMap<>();

    public static List<IFilter> createFilters(Object filterHolder) {
        if (filterHolder == null) {
            return Collections.emptyList();
        }

        List<IFilter> filters = new ArrayList<>();

        Class<? extends Object> holderType = filterHolder.getClass();
        List<FieldInfo> fieldInfos = fieldInfoMap.computeIfAbsent(holderType, type -> createFieldInfos(holderType));
        for (FieldInfo fieldInfo : fieldInfos) {
            Object fieldValue = ReflectUtil.invoke(fieldInfo.getFieldGetter(), filterHolder);
            if (fieldValue == null) {
                continue;
            }
            IFilterProcessor processor = fieldInfo.getProcessor();
            processor.collectFilters(fieldInfo.ftAnnotation, fieldValue, filters);
        }

        return filters;
    }

    private static List<FieldInfo> createFieldInfos(Class<?> holderType) {
        List<FieldInfo> fieldInfos = new ArrayList<>();
        for (Field field : holderType.getDeclaredFields()) {
            FieldInfo fieldInfo = findFilterAnnotation(field);
            if (fieldInfo != null) {
                fieldInfos.add(fieldInfo);
            }
        }
        return fieldInfos;
    }

    private static FieldInfo findFilterAnnotation(Field field) {
        for (Annotation annotation : field.getDeclaredAnnotations()) {
            FilterProcessor processorAnn = annotation.annotationType().getDeclaredAnnotation(FilterProcessor.class);
            if (processorAnn == null) {
                continue;
            }

            Class<? extends IFilterProcessor> processorType = processorAnn.value();
            IFilterProcessor processor = processorMap.computeIfAbsent(processorType, type -> ReflectUtil.newInstance(processorType));

            Method fieldGetter = ReflectUtil.findGetter(field);
            return new FieldInfo(field, annotation, processor, fieldGetter);
        }
        return null;
    }

    @Getter
    @AllArgsConstructor
    private static class FieldInfo {
        private Field field;
        private Annotation ftAnnotation;
        private IFilterProcessor processor;
        private Method fieldGetter;
    }
}
