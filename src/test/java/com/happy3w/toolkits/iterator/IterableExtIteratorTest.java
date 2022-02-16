package com.happy3w.toolkits.iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class IterableExtIteratorTest {

    @Test
    public void should_lazy_iterator() {
        Set<String> set = new HashSet<>(Arrays.asList("A", "B", "C", "D", "E"));
        String result = EasyIterator.of("C", "D")
                .peek(item -> set.remove(item))
                .concat(EasyIterator.fromIterable(set))
                .stream()
                .collect(Collectors.joining());
        Assertions.assertEquals("CDABE", result);
    }
}
