package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdConfig;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.persistence.core.rowdata.UnknownColumnStrategy;
import com.happy3w.toolkits.utils.ListUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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

    public <C extends IRdConfig> AbstractRdTableDef<RowData, ColType, Self> config(C config) {
        extConfigs.regist(config);
        return this;
    }

    public AbstractRdTableDef<RowData, ColType, Self> filterColumns(String... columnCode) {
        return filterColumns(Arrays.asList(columnCode));
    }

    public AbstractRdTableDef<RowData, ColType, Self> filterColumns(Collection<String> columnCodes) {
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
        return this;
    }
}
