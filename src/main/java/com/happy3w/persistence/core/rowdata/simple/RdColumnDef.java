package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RdColumnDef implements IRdColumnDef {
    private String code;
    private String title;
    private Class<?> dataType;
    private boolean required;
    private ExtConfigs extConfigs;

    public RdColumnDef(String code, String title) {
        this(code, title, String.class, false);
    }

    public RdColumnDef(String code, String title, Class<?> dataType, boolean required) {
        this.code = code;
        this.title = title;
        this.dataType = dataType;
        this.required = required;
        extConfigs = new ExtConfigs();
    }
}
