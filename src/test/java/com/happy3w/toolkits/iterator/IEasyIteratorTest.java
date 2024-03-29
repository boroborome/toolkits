package com.happy3w.toolkits.iterator;

import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class IEasyIteratorTest {

    @Test
    public void should_concat_easy_it_success() {
        List<String> result = EasyIterator.of("1")
                .concat(EasyIterator.of("2", "3"))
                .toList();
        Assertions.assertEquals("[\"1\",\"2\",\"3\"]", JSON.toJSONString(result));
    }

    @Test
    public void should_concat_it_success() {
        List<String> result = EasyIterator.of("1")
                .concat(Arrays.asList("2", "3").iterator())
                .toList();
        Assertions.assertEquals("[\"1\",\"2\",\"3\"]", JSON.toJSONString(result));
    }

    @Test
    public void should_concat_empty_success() {
        String[] items = {"2"};
        List<String> result = EasyIterator.of("1", null, "3")
                .concat(EasyIterator.of(items))
                .toList();
        Assertions.assertEquals("[\"1\",null,\"3\",\"2\"]", JSON.toJSONString(result));
    }

    @Test
    public void should_end_with_predicate() {
        String result = EasyIterator.of("1", "2", "3", "4")
                .endWhen(item -> Objects.equals(item, "3"))
                .foldLeftC(new StringBuilder(), (b, s) -> b.append(s))
                .toString();
        Assertions.assertEquals("12", result);
    }

    @Test
    public void should_distinct_success() {
        String result = EasyIterator.of("1", "2", "1", "3")
                .distinct()
                .foldLeftC(new StringBuilder(), (b, s) -> b.append(s))
                .toString();
        Assertions.assertEquals("123", result);
    }
}
