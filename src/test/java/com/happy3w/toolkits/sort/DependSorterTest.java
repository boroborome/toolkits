package com.happy3w.toolkits.sort;

import com.happy3w.java.ext.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DependSorterTest {

    @Test
    public void should_sort_success() {
        String result = DependSorter.sort(
                        Arrays.asList(
                                new DependItem("C", Arrays.asList("D")),
                                new DependItem("D", Arrays.asList("A", "B")),
                                new DependItem("B", Arrays.asList("A")),
                                new DependItem("A", Collections.EMPTY_LIST),
                                new DependItem("E", Arrays.asList("C", "B"))
                        ),
                        d -> new HashSet<>(d.getNeeds()),
                        d -> new HashSet<>(Arrays.asList(d.getName())))
                .stream()
                .map(DependItem::getName)
                .collect(Collectors.joining());
        Assert.assertEquals("ABDCE", result);
    }


    @Test
    public void should_sort_by_depend_success() {
        String result = DependSorter.sortByNeed(Arrays.asList(
                                new Pair<>("C", "D"),
                                new Pair<>("D", "A"),
                                new Pair<>("D", "B"),
                                new Pair<>("B", "A"),
                                new Pair<>("E", "C"),
                                new Pair<>("E", "B")
                        )
                )
                .stream()
                .collect(Collectors.joining());
        Assert.assertEquals("ABDCE", result);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class DependItem {
        private String name;
        private List<String> needs;

        @Override
        public String toString() {
            return name;
        }
    }
}
