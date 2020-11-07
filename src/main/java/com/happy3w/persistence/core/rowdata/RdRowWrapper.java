package com.happy3w.persistence.core.rowdata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class RdRowWrapper<T> {
    private T data;
    private String pageName;
    private int rowIndex;

    @Setter
    private String error;

    public RdRowWrapper(T data, String pageName, int rowIndex) {
        this.data = data;
        this.pageName = pageName;
        this.rowIndex = rowIndex;
    }

    public <D> RdRowWrapper<D> withNewData(D newData) {
        RdRowWrapper<D> newWrapper = new RdRowWrapper<D>(newData, pageName, rowIndex);
        newWrapper.error = error;
        return newWrapper;
    }
}
