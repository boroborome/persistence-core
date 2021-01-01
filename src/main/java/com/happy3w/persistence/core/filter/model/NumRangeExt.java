package com.happy3w.persistence.core.filter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumRangeExt<DT extends Number> extends NumRange<DT> {
    private boolean includeStart;
    private boolean includeEnd;
}
