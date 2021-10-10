package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.java.ext.ListUtils;
import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IColumnMatcher;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdConfig;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.persistence.core.rowdata.UnknownColumnStrategy;
import com.happy3w.persistence.core.rowdata.column.FixColumnMatcher;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public abstract class AbstractRdTableDef<RowData, ColType extends IRdColumnDef, Self extends AbstractRdTableDef<RowData, ColType, Self>>
        implements IRdTableDef<RowData, ColType> {
    @Getter
    @Setter
    protected List<ColType> columns;

    @Getter
    @Setter
    protected ExtConfigs extConfigs = new ExtConfigs();

    @Getter
    @Setter
    protected Supplier<IColumnMatcher> columnMatcherSupplier = () -> new FixColumnMatcher(this);

    @Override
    public IColumnMatcher createColumnMatcher() {
        return columnMatcherSupplier.get();
    }

    @Getter
    @Setter
    protected UnknownColumnStrategy unknownColumnStrategy = UnknownColumnStrategy.ignore;

    protected Map<String, ColType> titleColumnMap;
    protected Map<String, ColType> codeColumnMap;

    public ColType findByCode(String field) {
        return getCodeColumnMap().get(field);
    }

    protected Map<String, ColType> getCodeColumnMap() {
        if (codeColumnMap == null) {
            codeColumnMap = ListUtils.toMap(columns, IRdColumnDef::getCode);
        }
        return codeColumnMap;
    }

    public ColType findByTitle(String title) {
        if (titleColumnMap == null) {
            titleColumnMap = ListUtils.toMap(columns, IRdColumnDef::getTitle);
        }
        return titleColumnMap.get(title);
    }

    public <C extends IRdConfig> Self config(C config) {
        extConfigs.regist(config);
        return (Self) this;
    }

    public Self filterColumns(String... columnCode) {
        return filterColumns(Arrays.asList(columnCode));
    }

    public Self filterColumns(Collection<String> columnCodes) {
        Map<String, ColType> codeColMap = getCodeColumnMap();
        List<ColType> newColumns = new ArrayList<>();
        List<String> unknownColumns = new ArrayList<>();
        for (String code : columnCodes) {
            ColType colDef = codeColMap.get(code);
            if (colDef == null) {
                unknownColumns.add(code);
            }
            newColumns.add(colDef);
        }

        if (!unknownColumns.isEmpty()) {
            throw new IllegalArgumentException("Unknown column:" + unknownColumns);
        }
        this.columns = newColumns;
        return (Self) this;
    }
}
