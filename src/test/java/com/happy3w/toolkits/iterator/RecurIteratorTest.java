package com.happy3w.toolkits.iterator;

import lombok.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecurIteratorTest {

    @Test
    void should_recur_success() {
        Set<String> values = EasyIterator.of(MyData.builder()
                                .name("f1")
                                .subItems(Arrays.asList(MyData.builder()
                                        .name("f4")
                                        .build()))
                                .build(),
                        MyData.builder()
                                .name("f2")
                                .subItems(Arrays.asList(
                                        MyData.builder()
                                                .name("f3")
                                                .build()
                                ))
                                .build()
                )
                .recurFlatList(MyData::getSubItems)
                .map(MyData::getName)
                .toSet();

        Assertions.assertEquals(new HashSet<>(Arrays.asList("f1", "f2", "f3", "f4")), values);
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class MyData {
        private String name;
        private List<MyData> subItems;
    }
}