package com.happy3w.persistence.core.filter;

import com.alibaba.fastjson.JSON;
import com.happy3w.persistence.core.filter.ann.FieldEqual;
import com.happy3w.persistence.core.filter.ann.FieldLike;
import com.happy3w.persistence.core.filter.ann.FieldRange;
import com.happy3w.persistence.core.filter.model.NumRange;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FilterAssistantTest {

    @Test
    public void should_collect_filter_success() {
        MyFilter myFilter = MyFilter.builder()
                .name("Tom")
                .evaluate("Great")
                .age(12)
                .weightRange(new NumRange<>(3.4, 50.0))
                .build();
        List<IFilter> filters = FilterAssistant.createFilters(myFilter);
        Assertions.assertEquals("[{\"field\":\"name\",\"positive\":true,\"ref\":\"Tom\",\"type\":\"str-equal\"},{\"field\":\"evaluate\",\"positive\":true,\"ref\":\"Great\",\"type\":\"str-like\"},{\"field\":\"age\",\"positive\":true,\"ref\":12,\"type\":\"num-equal\"},{\"end\":50.0,\"field\":\"weight\",\"includeEnd\":false,\"includeStart\":true,\"positive\":true,\"start\":3.4,\"type\":\"num-range\"}]",
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

        @FieldEqual("age")
        private int age;

        @FieldRange("weight")
        private NumRange<Double> weightRange;
    }
}
