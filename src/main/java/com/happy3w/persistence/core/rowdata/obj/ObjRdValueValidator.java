package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ObjRdValueValidator {
    private boolean notEmpty;

    public <T> void validate(Object value, ObjRdColumnDef columnDefinition, ObjRdRowWrapper<T> wrapper, MessageRecorder messageRecorder) {
        if (notEmpty && isEmptyValue(value)) {
            messageRecorder.appendError(
                    "Field:{0} is required at line {1} in page {2}.",
                    columnDefinition.getTitle(),
                    wrapper.getRowIndex(),
                    wrapper.getPageName());
        }
    }

    public static boolean isEmptyValue(Object value) {
        if (value == null) {
            return true;
        }
        if (value instanceof String) {
            String s = (String) value;
            return StringUtils.isEmpty(s);
        }
        return false;
    }

    public static ObjRdValueValidator from(ObjRdValueValidation valueValidation) {
        if (valueValidation == null) {
            return null;
        }

        return new ObjRdValueValidator(valueValidation.notEmpty());
    }
}
