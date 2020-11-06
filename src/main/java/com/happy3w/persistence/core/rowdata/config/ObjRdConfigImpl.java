package com.happy3w.persistence.core.rowdata.config;

import com.happy3w.persistence.core.rowdata.IAnnotationRdConfig;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZoneId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjRdConfigImpl implements IAnnotationRdConfig<ObjRdConfigImpl, ObjRdConfig> {
    private ZoneId zoneId;
    private String dateFormatPattern;
    private String numFormat;

    @Override
    public void merge(ObjRdConfigImpl otherExtend) {
        if (otherExtend == null) {
            return;
        }

        if (otherExtend.getZoneId() != null) {
            zoneId = otherExtend.getZoneId();
        }

        if (StringUtils.hasText(otherExtend.getDateFormatPattern())) {
            dateFormatPattern = otherExtend.getDateFormatPattern();
        }

        if (StringUtils.hasText(otherExtend.getNumFormat())) {
            numFormat = otherExtend.getNumFormat();
        }
    }

    @Override
    public void initBy(ObjRdConfig annotation) {
        numFormat = StringUtils.emptyToNull(annotation.numFormat());
        dateFormatPattern = StringUtils.emptyToNull(annotation.dateFormatPattern());

        if (StringUtils.hasText(annotation.zoneId())) {
            zoneId = ZoneId.of(annotation.zoneId()).normalized();
        } else {
            zoneId = null;
        }
    }

}
