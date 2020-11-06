package com.happy3w.persistence.core.rowdata.simple;

import com.happy3w.persistence.core.rowdata.ExtConfigs;
import com.happy3w.persistence.core.rowdata.IRdColumnDef;
import com.happy3w.persistence.core.rowdata.IRdConfig;
import com.happy3w.persistence.core.rowdata.IRdTableDef;
import com.happy3w.toolkits.utils.ListUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractRdTableDef<T extends IRdColumnDef, M extends AbstractRdTableDef<T, M>> implements IRdTableDef<T> {
    @Getter
    @Setter
    protected List<T> columns;

    @Getter
    @Setter
    protected ExtConfigs extConfigs = new ExtConfigs();


    protected Map<String, T> titleColumnMap;
    protected Map<String, T> codeColumnMap;

    public T findByCode(String field) {
        return getCodeColumnMap().get(field);
    }

    protected Map<String, T> getCodeColumnMap() {
        if (codeColumnMap == null) {
            codeColumnMap = ListUtils.toMap(columns, IRdColumnDef::getCode);
        }
        return codeColumnMap;
    }

    public T findByTitle(String title) {
        if (titleColumnMap == null) {
            titleColumnMap = ListUtils.toMap(columns, IRdColumnDef::getTitle);
        }
        return titleColumnMap.get(title);
    }

    public <C extends IRdConfig> AbstractRdTableDef<T, M> config(C config) {
        extConfigs.save(config);
        return this;
    }

    public AbstractRdTableDef<T, M> filterColumns(String... columnCode) {
        return filterColumns(Arrays.asList(columnCode));
    }

    public AbstractRdTableDef<T, M> filterColumns(Collection<String> columnCodes) {
        Map<String, T> codeColMap = getCodeColumnMap();
        List<T> newColumns = new ArrayList<>();
        List<String> unknownColumns = new ArrayList<>();
        for (String code : columnCodes) {
            T colDef = codeColMap.get(code);
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
