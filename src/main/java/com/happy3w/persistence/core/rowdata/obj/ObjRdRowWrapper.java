package com.happy3w.persistence.core.rowdata.obj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class ObjRdRowWrapper<T> {
    private T data;
    private String pageName;
    private int rowIndex;

    @Setter
    private String error;

    public ObjRdRowWrapper(T data, String pageName, int rowIndex) {
        this.data = data;
        this.pageName = pageName;
        this.rowIndex = rowIndex;
    }
}
