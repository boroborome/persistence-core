package com.happy3w.persistence.core.rowdata.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
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
        try {
            PropertyDescriptor desc = new PropertyDescriptor(field.getName(), field.getDeclaringClass());
            return new ObjRdValueAccessor(field.getName(), field.getType(),
                    desc.getReadMethod(), desc.getWriteMethod());
        } catch (IntrospectionException e) {
            throw new RuntimeException("No Property define for field:" + field.toString(), e);
        }
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
