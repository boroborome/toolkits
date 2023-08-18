package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class StartActionIteratorTest {

    @Test
    void should_run_before_start_success() {
        List<String> records = new ArrayList<>();

        EasyIterator.of("A", "B")
                .onStart(v -> records.add("S" + v))
                .forEach(v -> records.add(v));

        Assertions.assertEquals(
                Arrays.asList("SA", "A", "B"),
                records
        );
    }

    @Test
    void should_not_run_before_start_when_no_data() {
        List<String> records = new ArrayList<>();

        EasyIterator.<String>emptyIterator()
                .onStart(v -> records.add("S" + v))
                .forEach(v -> records.add(v));

        Assertions.assertEquals(
                Arrays.asList(),
                records
        );
    }
}