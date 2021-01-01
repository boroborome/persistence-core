package com.happy3w.persistence.core.filter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NumRange<DT extends Number> {
    protected DT start;
    protected DT end;
}
