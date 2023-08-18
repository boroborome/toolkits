package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class LongRangeIteratorTest {

    @Test
    void should_enum_range_success() {
        Assertions.assertEquals(
                Arrays.asList(0L, 1L, 2L),
                EasyIterator.range(0, 3)
                        .toList()
        );

        Assertions.assertEquals(
                Arrays.asList(3L, 2L, 1L),
                EasyIterator.range(3, 0, -1)
                        .toList()
        );
    }
}