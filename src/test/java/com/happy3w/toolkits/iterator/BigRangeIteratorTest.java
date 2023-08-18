package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;

class BigRangeIteratorTest {

    @Test
    void should_enum_range_success() {
        Assertions.assertEquals(
                Arrays.asList("0", "1", "2"),
                EasyIterator.bigRange(BigInteger.ZERO, new BigInteger("3"))
                        .map(n -> n.toString())
                        .toList()
        );

        Assertions.assertEquals(
                Arrays.asList("3", "2", "1"),
                EasyIterator.bigRange(new BigInteger("3"), BigInteger.ZERO, new BigInteger("-1"))
                        .map(n -> n.toString())
                        .toList()
        );
    }
}