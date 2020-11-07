package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.persistence.core.rowdata.config.DateFormat;
import com.happy3w.persistence.core.rowdata.config.DateFormatImpl;
import com.happy3w.persistence.core.rowdata.config.DateZoneId;
import com.happy3w.persistence.core.rowdata.config.DateZoneIdImpl;
import com.happy3w.persistence.core.rowdata.config.NumFormat;
import com.happy3w.persistence.core.rowdata.config.NumFormatImpl;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.ListUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ObjRdTableDefTest {

    @Test
    public void should_init_config_by_class_success() {
        ObjRdTableDef<MyData> tableDef = ObjRdTableDef.from(MyData.class);

        ObjRdColumnDef nameField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "name");
        ObjRdColumnDef ageField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "age");
        ObjRdColumnDef enabledField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "enabled");
        ObjRdColumnDef birthdayField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "birthday");

        Assert.assertEquals("UTC+8", tableDef.getExtConfigs().getConfig(DateZoneIdImpl.class).getZoneId());
        Assert.assertEquals(true, nameField.isRequired());
        Assert.assertEquals(false, ageField.isRequired());
        Assert.assertEquals("000", ageField.getExtConfigs().getConfig(NumFormatImpl.class).getFormat());
        Assert.assertEquals("000", ageField.getExtConfigs().getConfig(NumFormatImpl.class).getFormat());
        Assert.assertEquals("setEnabledText", enabledField.getAccessor().getSetMethod().getName());
        Assert.assertEquals("yyyy-MM-dd", birthdayField.getExtConfigs().getConfig(DateFormatImpl.class).getFormat());
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @DateZoneId("UTC+8")
    public static class MyData {
        @ObjRdColumn(value = "名字")
        private String name;

        @ObjRdColumn(value = "年龄", required = false)
        @NumFormat("000")
        private int age;

        @ObjRdColumn(value = "在校生", setter = "setEnabledText")
        private boolean enabled;

        @ObjRdColumn("生日")
        @DateFormat("yyyy-MM-dd")
        private Date birthday;

        @ObjRdPostAction
        public void postInit(RdRowWrapper<MyData> data, MessageRecorder recorder) {

        }

        public void setEnabledText(String enabled) {

        }
    }
}
