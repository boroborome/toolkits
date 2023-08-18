package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ItemEndActionIteratorTest {

    @Test
    void should_item_end_success() {
        List<String> items = Arrays.asList("1", "2");

        List<String> log = new ArrayList<>();
        EasyIterator.fromIterable(items)
                .onItemEnd(item -> log.add(item + "-end"))
                .forEach(item -> log.add(item));

        Assertions.assertEquals(
                Arrays.asList("1",
                        "1-end",
                        "2",
                        "2-end"),
                log);
    }
}