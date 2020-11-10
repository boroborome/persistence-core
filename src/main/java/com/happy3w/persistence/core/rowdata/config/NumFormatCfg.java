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
public class NumFormatCfg implements IAnnotationRdConfig<NumFormat> {
    private String format;
    @Override
    public void initBy(NumFormat annotation) {
        this.format = annotation.value();
    }

    @Override
    public void buildContentKey(StringBuilder builder) {
        builder.append(format);
    }
}
