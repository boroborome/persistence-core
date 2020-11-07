package com.happy3w.persistence.core.rowdata.obj;

import com.happy3w.persistence.core.rowdata.page.IDataPage;
import com.happy3w.persistence.core.rowdata.page.IReadDataPage;
import com.happy3w.persistence.core.rowdata.page.IWriteDataPage;
import com.happy3w.persistence.core.rowdata.RdRowIterator;
import com.happy3w.persistence.core.rowdata.RdRowWrapper;
import com.happy3w.toolkits.message.MessageRecorder;
import com.happy3w.toolkits.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class ObjRdAssistant {
    private IDataPage dataPage;
    private IReadDataPage readPage;
    private IWriteDataPage writePage;

    private final MessageRecorder messageRecorder;

    public ObjRdAssistant(IDataPage dataPage, MessageRecorder messageRecorder) {
        this.dataPage = dataPage;
        this.messageRecorder = messageRecorder;

        if (dataPage instanceof IReadDataPage) {
            readPage = (IReadDataPage) dataPage;
        }
        if (dataPage instanceof IWriteDataPage) {
            writePage = (IWriteDataPage) dataPage;
        }
    }

    private IReadDataPage getReadPage() {
        if (readPage == null) {
            throw new UnsupportedOperationException("Current page is not support read." + dataPage);
        }
        return readPage;
    }

    private IWriteDataPage getWritePage() {
        if (writePage == null) {
            throw new UnsupportedOperationException("Current page is not support write." + dataPage);
        }
        return writePage;
    }

    public <T> List<T> loadPage(Class<T> dataType) {
        return loadPage(dataType, w -> w.getData());
    }

    public <T> List<T> loadPage(Class<T> dataType, Function<RdRowWrapper<T>, T> checker) {
        ObjRdTableDef<T> objDefinition = ObjRdTableDef.from(dataType);

        List<T> allData = new ArrayList<>();
        RdRowIterator<T> dataIt = RdRowIterator.from(getReadPage(), objDefinition, messageRecorder);
        dataIt.forEachRemaining(w -> {
            T data = checker.apply(w);
            if (data != null) {
                allData.add(w.getData());
            }
        });
        return allData;
    }

    public static ObjRdAssistant from(IDataPage dataPage) {
        return new ObjRdAssistant(dataPage, new MessageRecorder());
    }

    public static ObjRdAssistant from(IDataPage dataPage, MessageRecorder messageRecorder) {
        return new ObjRdAssistant(dataPage, messageRecorder);
    }

    public <T> ObjRdAssistant savePage(List<T> datas, Class<T> dataType) {
        return savePage(datas.stream(), dataType);
    }

    public <T> ObjRdAssistant savePage(Stream<T> datas, Class<T> dataType) {
        ObjRdTableDef<T> objDef = ObjRdTableDef.from(dataType);

        List<String> titles = ListUtils.map(objDef.getColumns(), ObjRdColumnDef::getTitle);
        getWritePage().writeList(titles)
            .newLine();

        datas.forEach(data -> {
            List<Object> values = ListUtils.map(objDef.getColumns(), c -> c.getAccessor().getValue(data));
            getWritePage().writeList(values)
                .newLine();
        });
        return this;
    }
}
