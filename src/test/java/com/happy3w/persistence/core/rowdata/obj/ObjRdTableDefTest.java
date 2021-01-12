package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.persistence.core.rowdata.config.DateFormat;
import com.happy3w.persistence.core.rowdata.config.DateFormatCfg;
import com.happy3w.persistence.core.rowdata.config.DateZoneId;
import com.happy3w.persistence.core.rowdata.config.DateZoneIdCfg;
import com.happy3w.persistence.core.rowdata.config.NumFormat;
import com.happy3w.persistence.core.rowdata.config.NumFormatCfg;
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

        Assert.assertEquals("UTC+8", tableDef.getExtConfigs().getConfig(DateZoneIdCfg.class).getZoneId());
        Assert.assertEquals(true, nameField.isRequired());
        Assert.assertEquals(false, ageField.isRequired());
        Assert.assertEquals("000", ageField.getExtConfigs().getConfig(NumFormatCfg.class).getFormat());
        Assert.assertEquals("000", ageField.getExtConfigs().getConfig(NumFormatCfg.class).getFormat());
        Assert.assertEquals("setEnabledText", enabledField.getAccessor().getSetMethod().getName());
        Assert.assertEquals("yyyy-MM-dd", birthdayField.getExtConfigs().getConfig(DateFormatCfg.class).getFormat());
    }

    @Test
    public void should_init_config_by_sub_class_success() {
        ObjRdTableDef<SubClass> tableDef = ObjRdTableDef.from(SubClass.class);

        ObjRdColumnDef subNameField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "subName");
        Assert.assertNotNull(subNameField);

        ObjRdColumnDef ageField = ListUtils.findFirstMatch(tableDef.getColumns(), ObjRdColumnDef::getCode, "age");
        Assert.assertNotNull(ageField);
    }

    @Getter
    @Setter
    public static class SubClass extends MyData {
        @ObjRdColumn(value = "subName")
        private String subName;
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

        @ObjRdColumn(value = "在校生", setter = "setEnabledText", getter = "getEnabledText")
        private boolean enabled;

        @ObjRdColumn("生日")
        @DateFormat("yyyy-MM-dd")
        private Date birthday;

        @ObjRdPostAction
        public void postInit(RdRowWrapper<MyData> data, MessageRecorder recorder) {

        }

        public String getEnabledText() {
            return String.valueOf(enabled);
        }

        public void setEnabledText(String enabledStr) {
            enabled = Boolean.parseBoolean(enabledStr);
        }
    }
}
