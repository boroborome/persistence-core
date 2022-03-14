package com.happy3w.persistence.core.rowdata;

import com.alibaba.fastjson.JSON;
import com.happy3w.persistence.core.rowdata.column.DynamicColumnMatcher;
import com.happy3w.persistence.core.rowdata.obj.ObjRdTableDef;
import com.happy3w.persistence.core.rowdata.page.MemReadDataPage;
import com.happy3w.persistence.core.rowdata.simple.ListRdTableDef;
import com.happy3w.persistence.core.rowdata.simple.MapRdTableDef;
import com.happy3w.toolkits.message.MessageRecorder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public class RdRowIteratorTest {

    @Test
    public void should_load_normal_data_success() {
        MemReadDataPage readDataPage = new MemReadDataPage()
                .pageName("test-page")
                .rowData("名字", "生日", "体重", "更新时间", "在校生")
                .rowData("Jerry", null, 3.4d, Timestamp.valueOf("2020-12-20 00:00:00"), "在校");

        ObjRdTableDef<Student> dataDef = ObjRdTableDef.from(Student.class);

        MessageRecorder recorder = new MessageRecorder();
        RdRowIterator<Student> it = new RdRowIterator<>(readDataPage, dataDef, recorder);
        List<Student> datas = it.map(RdRowWrapper::getData)
                .toList();

        Assertions.assertEquals("[]", JSON.toJSONString(recorder.getErrors()));
        Assertions.assertEquals("[{\"name\":\"Jerry\",\"studying\":true,\"updateTime\":1608393600000,\"weight\":3.4}]",
                JSON.toJSONString(datas));
    }

    @Test
    public void should_load_by_ListRdTableDef_success() {
        MemReadDataPage readDataPage = new MemReadDataPage()
                .pageName("test-page")
                .rowData("名字", "生日", "体重", "更新时间", "在校生")
                .rowData("Jerry", null, 3.4d, Timestamp.valueOf("2020-12-20 00:00:00"), "在校");

        ListRdTableDef dataDef = new ListRdTableDef();
        dataDef.setColumnMatcherSupplier(() -> new DynamicColumnMatcher(dataDef));

        MessageRecorder recorder = new MessageRecorder();
        List<List<Object>> datas = new RdRowIterator<>(readDataPage, dataDef, recorder)
                .map(RdRowWrapper::getData)
                .toList();

        Assertions.assertEquals("[]", JSON.toJSONString(recorder.getErrors()));
        Assertions.assertEquals("[[\"Jerry\",\"\",\"3.4\",\"2020-12-20 00:00:00\",\"在校\"]]",
                JSON.toJSONString(datas));
    }


    @Test
    public void should_load_by_MapRdTableDef_success() {
        MemReadDataPage readDataPage = new MemReadDataPage()
                .pageName("test-page")
                .rowData("名字", "生日", "体重", "更新时间", "在校生")
                .rowData("Jerry", null, 3.4d, Timestamp.valueOf("2020-12-20 00:00:00"), "在校");

        MapRdTableDef dataDef = new MapRdTableDef();
        dataDef.setColumnMatcherSupplier(() -> new DynamicColumnMatcher(dataDef));

        MessageRecorder recorder = new MessageRecorder();
        List<Map<String, Object>> datas = new RdRowIterator<>(readDataPage, dataDef, recorder)
                .map(RdRowWrapper::getData)
                .toList();

        Assertions.assertEquals("[]", JSON.toJSONString(recorder.getErrors()));
        Assertions.assertEquals("[{\"体重\":\"3.4\",\"名字\":\"Jerry\",\"在校生\":\"在校\",\"生日\":\"\",\"更新时间\":\"2020-12-20 00:00:00\"}]",
                JSON.toJSONString(datas));
    }
}
