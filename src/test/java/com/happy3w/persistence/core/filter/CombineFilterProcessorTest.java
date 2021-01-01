package com.happy3w.persistence.core.filter;

import com.happy3w.persistence.core.filter.impl.StringEqualFilter;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombineFilterProcessorTest {

    @Test
    public void given_known_ref_then_return_filter() {
        CombineFilterProcessor combineFilterProcessor = new CombineFilterProcessor();
        combineFilterProcessor.registProcessor(String.class, new StubProcessor());
        Annotation stubFilterAnn = Mockito.mock(Annotation.class);

        List<IFilter> filters = new ArrayList<>();
        combineFilterProcessor.collectFilters(stubFilterAnn, "stubRef", filters);

        Assert.assertEquals(1, filters.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void given_unknown_ref_then_throw_exception() {
        CombineFilterProcessor combineFilterProcessor = new CombineFilterProcessor();
        combineFilterProcessor.registProcessor(String.class, new StubProcessor());
        Annotation stubFilterAnn = Mockito.mock(Annotation.class);

        List<IFilter> filters = new ArrayList<>();
        combineFilterProcessor.collectFilters(stubFilterAnn, 100, filters);

        // Expect exception
    }


    @Test
    public void given_known_collect_ref_then_return_filter() {
        CombineFilterProcessor combineFilterProcessor = new CombineFilterProcessor();
        combineFilterProcessor.registCollectionProcessor(String.class, new StubProcessor());
        Annotation stubFilterAnn = Mockito.mock(Annotation.class);

        List<IFilter> filters = new ArrayList<>();
        combineFilterProcessor.collectFilters(stubFilterAnn, Arrays.asList("stubRef"), filters);

        Assert.assertEquals(1, filters.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void given_unknown_collect_ref_then_throw_exception() {
        CombineFilterProcessor combineFilterProcessor = new CombineFilterProcessor();
        combineFilterProcessor.registCollectionProcessor(String.class, new StubProcessor());
        Annotation stubFilterAnn = Mockito.mock(Annotation.class);

        List<IFilter> filters = new ArrayList<>();
        combineFilterProcessor.collectFilters(stubFilterAnn, Arrays.asList(100), filters);

        // Expect exception
    }

    private static class StubProcessor implements IFilterProcessor {

        @Override
        public void collectFilters(Annotation ftAnnotation, Object ftValue, List filters) {
            filters.add(new StringEqualFilter("xxx", "xxx"));
        }
    }
}
