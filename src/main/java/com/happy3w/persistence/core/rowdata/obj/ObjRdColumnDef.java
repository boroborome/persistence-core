package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdConfig;
import com.happy3w.toolkits.utils.ListUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.List;
import java.util.Map;

/**
 * 将一个对象上的字段当做一列处理的ColumnDef
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class ObjRdColumnDef implements IRdColumnDef {
    private String title;
    private boolean required;
    private ObjRdValueValidator validator;
    private ObjRdValueAccessor accessor;

    @Builder.Default
    private ExtConfigs extConfigs = new ExtConfigs();

    public <T extends IRdConfig> ObjRdColumnDef extConfig(T config) {
        extConfigs.save(config);
        return this;
    }

    @Override
    public String getCode() {
        return accessor.getFieldName();
    }

    /**
     * 根据fieldMap中内容创建一组column
     * @param fieldMap 需要创建column definition的字段信息，key为字段名称，value为title
     * @param dataType fieldMap中字段所在的数据类型
     * @return 所有column定义
     */
    public static List<ObjRdColumnDef> create(Map<String, String> fieldMap, Class dataType) {
        return ListUtils.map(fieldMap.entrySet(), entity -> createColumnDef(entity.getKey(), entity.getValue(), dataType));
    }

    /**
     * 直接通过字段名称创建ObjRdColumnDef
     * @param field 字段名称
     * @param title 输入文件的Title
     * @param dataType 字段所在数据类型
     * @return Column Def
     */
    public static ObjRdColumnDef createColumnDef(String field, String title, Class dataType) {
        try {
            PropertyDescriptor desc = new PropertyDescriptor(field, dataType);
            return ObjRdColumnDef.builder()
                    .title(title)
                    .required(false)
                    .accessor(new ObjRdValueAccessor(field,
                            desc.getPropertyType(),
                            desc.getReadMethod(),
                            desc.getWriteMethod()))
                    .build();
        } catch (IntrospectionException e) {
            throw new UnsupportedOperationException("Can't get field:" + field + " in type:" + dataType);
        }
    }
}
