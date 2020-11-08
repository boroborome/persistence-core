package com.happy3w.persistence.core.rowdata.config;

import com.happy3w.persistence.core.rowdata.IAnnotationRdConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DateZoneIdImpl implements IAnnotationRdConfig<DateZoneId> {
    private String zoneId;
    @Override
    public void initBy(DateZoneId annotation) {
        this.zoneId = annotation.value();
    }

    @Override
    public void buildContentKey(StringBuilder builder) {
        builder.append(zoneId);
    }
}
