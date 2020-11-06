package com.happy3w.persistence.core.rowdata.page;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractDataPage<T extends AbstractDataPage<T>> implements IDataPage<T> {
    @Getter
    @Setter
    protected ExtConfigs extConfigs = new ExtConfigs();

    protected int row;
    protected int column;

    @Override
    public T locate(int row, int column) {
        if (row < 0 || column < 0) {
            throw new IllegalArgumentException("Row and Column in excel can not small then 0.");
        }
        this.row = row;
        this.column = column;
        return (T) this;
    }
}
