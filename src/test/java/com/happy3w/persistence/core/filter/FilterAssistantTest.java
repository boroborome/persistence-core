package com.happy3w.persistence.core.filter;

import com.alibaba.fastjson.JSON;
import com.happy3w.persistence.core.filter.ann.FieldEqual;
import com.happy3w.persistence.core.filter.ann.FieldLike;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class FilterAssistantTest {

    @Test
    public void should_collect_filter_success() {
        MyFilter myFilter = new MyFilter("Tom", "Great");
        List<IFilter> filters = FilterAssistant.createFilters(myFilter);
        Assert.assertEquals("[{\"field\":\"name\",\"positive\":true,\"ref\":\"Tom\",\"type\":\"str-equal\"},{\"field\":\"evaluate\",\"positive\":true,\"ref\":\"Great\",\"type\":\"str-like\"}]",
                JSON.toJSONString(filters));
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    public static class MyFilter {
        @FieldEqual("name")
        private String name;

        @FieldLike("evaluate")
        private String evaluate;
    }
}
