package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ItemStartActionIteratorTest {

    @Test
    void should_item_start_success() {
        List<String> items = Arrays.asList("1", "2");

        List<String> log = new ArrayList<>();
        EasyIterator.fromIterable(items)
                .onItemStart(item -> log.add(item + "-start"))
                .forEach(item -> log.add(item));

        Assertions.assertEquals(
                Arrays.asList(
                        "1-start",
                        "1",
                        "2-start",
                        "2"
                ),
                log);
    }
}