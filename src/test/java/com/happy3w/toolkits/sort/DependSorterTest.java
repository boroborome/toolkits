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
    public void should_sort_success_with_no_depend() {
        String result = DependSorter.sort(
                        Arrays.asList(
                                new DependItem("C", Collections.EMPTY_LIST),
                                new DependItem("D", Collections.EMPTY_LIST),
                                new DependItem("B", Collections.EMPTY_LIST),
                                new DependItem("A", Collections.EMPTY_LIST),
                                new DependItem("E", Collections.EMPTY_LIST)
                        ),
                        d -> new HashSet<>(d.getNeeds()),
                        d -> new HashSet<>(Arrays.asList(d.getName())))
                .stream()
                .map(DependItem::getName)
                .collect(Collectors.joining());
        Assert.assertEquals(5, result.length());
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

    @Test(expected = IllegalArgumentException.class)
    public void should_error_with_circle() {
        // circle: D->E->C->D
        DependSorter.sortByNeed(Arrays.asList(
                                new Pair<>("C", "D"),
                                new Pair<>("D", "A"),
                                new Pair<>("D", "E"),
                                new Pair<>("D", "B"),
                                new Pair<>("B", "A"),
                                new Pair<>("E", "C"),
                                new Pair<>("E", "B")
                        )
                );
    }

    @Test
    public void should_sort_with_multi_graph_success() {
        String result = DependSorter.sortByNeed(Arrays.asList(
                                new Pair<>("C", "D"),
                                new Pair<>("D", "A"),
                                new Pair<>("D", "B"),
                                new Pair<>("B", "A"),
                                new Pair<>("E", "C"),
                                new Pair<>("E", "B"),
                                // The second graph
                                new Pair<>("F", "H"),
                                new Pair<>("G", "H")
                        )
                )
                .stream()
                .collect(Collectors.joining());
        Assert.assertEquals("AHBFGDCE", result);
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
