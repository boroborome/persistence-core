package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.toolkits.reflect.FieldAccessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Getter
@AllArgsConstructor
public class ObjRdValueAccessor {
    private String fieldName;
    private Class dataType;
    private Method getMethod;
    private Method setMethod;

    public static ObjRdValueAccessor from(Field field) {
        FieldAccessor fieldAccessor = FieldAccessor.from(field);
        return new ObjRdValueAccessor(fieldAccessor.getFieldName(),
                fieldAccessor.getDataType(),
                fieldAccessor.getGetMethod(),
                fieldAccessor.getSetMethod());
    }

    public Object getValue(Object data) {
        if (data == null) {
            return null;
        }
        try {
            return getMethod.invoke(data);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to read field:" + fieldName + " from:" + data, e);
        }
    }
}
