package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IAnnotationRdConfig;
import com.happy3w.persistence.core.rowdata.IRdConfig;
import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.persistence.core.rowdata.UnknownColumnStrategy;
import com.happy3w.persistence.core.rowdata.simple.AbstractRdTableDef;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.relect.ReflectUtil;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用于定义一个可以序列化到文件的对象
 * @param <T> 具体的数据类型
 */
@Getter
@Setter
@AllArgsConstructor
@Builder
public class ObjRdTableDef<T> extends AbstractRdTableDef<T, ObjRdColumnDef, ObjRdTableDef<T>> {
    private Class<T> dataType;
    private Method postAction;

    private ObjRdTableDef(Class<T> dataType) {
        this.dataType = dataType;
    }

    public void runPostAction(RdRowWrapper<T> wrapper, MessageRecorder messageRecorder) {
        if (postAction == null) {
            return;
        }
        List<Object> params = createMethodParams(postAction.getParameterTypes(), wrapper, messageRecorder);
        ReflectUtil.invoke(postAction, wrapper.getData(), params.toArray());
    }

    private List<Object> createMethodParams(Class[] paramTypes, RdRowWrapper<T> wrapper, MessageRecorder messageRecorder) {
        List<Object> params = new ArrayList<>();
        for (Class paramType : paramTypes) {
            if (paramType == RdRowWrapper.class) {
                params.add(wrapper);
            } else if (paramType == MessageRecorder.class) {
                params.add(messageRecorder);
            } else {
                params.add(null);
            }
        }
        return params;
    }

    public ObjRdTableDef<T> configNotEmptyColumn(String... columns) {
        return configNotEmptyColumn(Arrays.asList(columns));
    }

    public ObjRdTableDef<T> configNotEmptyColumn(Collection<String> columns) {
        Set<String> columnSet = new HashSet<>(columns);
        for (ObjRdColumnDef columnDefinition : this.columns) {
            ObjRdValueValidator valueValidator = columnSet.contains(columnDefinition.getTitle())
                    ? new ObjRdValueValidator(true)
                    : null;
            columnDefinition.setValidator(valueValidator);
        }
        return this;
    }

    public ObjRdTableDef<T> configRequiredColumn(String... columns) {
        return configRequiredColumn(Arrays.asList(columns));
    }

    public ObjRdTableDef<T> configRequiredColumn(Collection<String> columns) {
        Set<String> columnSet = new HashSet<>(columns);
        for (ObjRdColumnDef columnDefinition : this.columns) {
            columnDefinition.setRequired(columnSet.contains(columnDefinition.getTitle()));
        }
        return this;
    }

    @Override
    public List<Object> toColumnValues(T data) {
        List lst = new ArrayList<>();
        for (ObjRdColumnDef colDef : columns) {
            lst.add(colDef.getAccessor().getValue(data));
        }
        return lst;
    }

    @Override
    public RdRowWrapper<T> toRowData(RdRowWrapper<List<Object>> columnValuesWrapper, MessageRecorder messageRecorder) {
        T data = ReflectUtil.newInstance(dataType);
        RdRowWrapper<T> rowDataWrapper = columnValuesWrapper.withNewData(data);
        for (int i = 0; i < columns.size(); i++) {
            ObjRdColumnDef column = columns.get(i);
            Object columnValue = columnValuesWrapper.getData().get(i);

            validateValue(column, columnValue, rowDataWrapper, messageRecorder);
            saveColumnValue(column, columnValue, rowDataWrapper, messageRecorder);
        }
        runPostAction(rowDataWrapper, messageRecorder);
        return rowDataWrapper;
    }

    private void saveColumnValue(
            ObjRdColumnDef column,
            Object columnValue,
            RdRowWrapper<T> wrapper,
            MessageRecorder messageRecorder) {
        Method setter = column.getAccessor().getSetMethod();
        List<Object> params = createMethodParams(setter.getParameterTypes(), wrapper, messageRecorder);
        params.set(0, columnValue);
        ReflectUtil.invoke(setter, wrapper.getData(), params.toArray());
    }

    private void validateValue(ObjRdColumnDef columnDef, Object curValue, RdRowWrapper<T> wrapper, MessageRecorder messageRecorder) {
        ObjRdValueValidator validator = columnDef.getValidator();
        if (validator != null) {
            validator.validate(curValue, columnDef, wrapper, messageRecorder);
        }
    }

    public static <T> ObjRdTableDef<T> from(Class<T> dataType) {
        ObjRdTableDef<T> objectDefinition = new ObjRdTableDef<>(dataType);

        objectDefinition.columns = createColumnDefs(dataType);
        objectDefinition.postAction = findPostAction(dataType);
        objectDefinition.unknownColumnStrategy = findUnknownColumnStrategy(dataType);
        objectDefinition.extConfigs = createExtConfigs(dataType.getDeclaredAnnotations());

        return objectDefinition;
    }

    private static <T> ExtConfigs createExtConfigs(Annotation[] annotations) {
        Map<Class<? extends IRdConfig>, IRdConfig> extConfigs = new HashMap<>();
        for (Annotation configAnnotation : annotations) {
            ObjRdConfigMap mapAnnotation = configAnnotation.annotationType().getDeclaredAnnotation(ObjRdConfigMap.class);
            if (mapAnnotation == null) {
                continue;
            }
            IAnnotationRdConfig config = ReflectUtil.newInstance(mapAnnotation.value());
            config.initBy(configAnnotation);

            extConfigs.put(config.getClass(), config);
        }
        return new ExtConfigs(extConfigs);
    }

    private static <T> List<ObjRdColumnDef> createColumnDefs(Class<T> dataType) {
        List<ObjRdColumnDef> columns = new ArrayList<>();
        Method[] methods = dataType.getDeclaredMethods();
        for (Field field : dataType.getDeclaredFields()) {
            ObjRdColumn columnAnnotation = field.getDeclaredAnnotation(ObjRdColumn.class);
            if (columnAnnotation == null) {
                continue;
            }
            ObjRdColumnDef columnDef = createColumnDefinition(columnAnnotation, field, methods);
            columns.add(columnDef);
        }
        return columns;
    }

    private static ObjRdColumnDef createColumnDefinition(ObjRdColumn objRdColumn, Field field, Method[] methods) {
        return ObjRdColumnDef.builder()
                .title(objRdColumn.value())
                .required(objRdColumn.required())
                .accessor(createObjRdValueAccessor(methods, field, objRdColumn))
                .validator(createObjRdValueValidator(field))
                .extConfigs(createExtConfigs(field.getDeclaredAnnotations()))
                .build();
    }

    private static <T> UnknownColumnStrategy findUnknownColumnStrategy(Class<T> dataType) {
        ObjRdUnknownColumn unexpectedColumns = dataType.getAnnotation(ObjRdUnknownColumn.class);
        return unexpectedColumns == null
                ? UnknownColumnStrategy.ignore
                : unexpectedColumns.strategy();
    }

    private static ObjRdValueValidator createObjRdValueValidator(Field field) {
        ObjRdValueValidation valueValidation = field.getDeclaredAnnotation(ObjRdValueValidation.class);
        return valueValidation == null ? null : ObjRdValueValidator.from(valueValidation);
    }

    private static ObjRdValueAccessor createObjRdValueAccessor(Method[] methods, Field field, ObjRdColumn objRdColumn) {
        ObjRdValueAccessor accessor;
        Method getter;
        Method setter;
        if (StringUtils.isEmpty(objRdColumn.getter()) || StringUtils.isEmpty(objRdColumn.setter())) {
            accessor = ObjRdValueAccessor.from(field);
            getter = chooseMethod(objRdColumn.getter(), accessor.getGetMethod(), methods);
            setter = chooseMethod(objRdColumn.setter(), accessor.getSetMethod(), methods);
        } else {
            getter = ReflectUtil.findMethod(objRdColumn.getter(), methods);
            setter = ReflectUtil.findMethod(objRdColumn.setter(), methods);
        }
        accessor = new ObjRdValueAccessor(field.getName(), getter.getReturnType(), getter, setter);

        if (getter.getReturnType() != setter.getParameterTypes()[0]) {
            throw new UnsupportedOperationException(
                    MessageFormat.format("The type of getter and setter on property({0}.{1}) are not the same type.Getter is {2}, Setter is {3}.",
                            field.getDeclaringClass(),
                            field.getName(),
                            getter.getReturnType(),
                            setter.getParameterTypes()[0]));
        }
        return accessor;
    }

    private static Method chooseMethod(String methodName, Method defaultMethod, Method[] methods) {
        return StringUtils.isEmpty(methodName) ? defaultMethod : ReflectUtil.findMethod(methodName, methods);
    }

    private static Method findPostAction(Class dataType) {
        Method postAction = null;
        Method[] methods = dataType.getDeclaredMethods();
        for (Method method : methods) {
            ObjRdPostAction postActionAnnotation = method.getAnnotation(ObjRdPostAction.class);
            if (postActionAnnotation == null) {
                continue;
            }
            if (postAction != null) {
                throw new UnsupportedOperationException("Unsupported multiple ObjRdPostAction configured in one class:" + dataType);
            }
            postAction = method;
        }
        return postAction;
    }
}
