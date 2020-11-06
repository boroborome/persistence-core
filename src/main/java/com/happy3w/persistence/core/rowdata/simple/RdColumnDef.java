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
    private ExtConfigs extConfigs = new ExtConfigs();
}
