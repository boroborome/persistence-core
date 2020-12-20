package com.happy3w.persistence.core.rowdata;

import com.alibaba.fastjson.JSON;
import com.happy3w.persistence.core.rowdata.obj.ObjRdTableDef;
import com.happy3w.persistence.core.rowdata.page.MemReadDataPage;
import com.happy3w.toolkits.message.MessageRecorder;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.List;

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

        Assert.assertEquals("[]", JSON.toJSONString(recorder.getErrors()));
        Assert.assertEquals("[{\"name\":\"Jerry\",\"studying\":true,\"updateTime\":1608393600000,\"weight\":3.4}]",
                JSON.toJSONString(datas));
    }
}
