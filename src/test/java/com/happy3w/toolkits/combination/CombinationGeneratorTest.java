package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CombinationGeneratorTest {

    @Test
    public void should_used_in_composite_scene_success() {
        DimensionManager<String, String> dimensionManager = new DimensionManager<>(
                Pair.ofList("d1", "1", "2"),
                Pair.ofList("d2", "d2v1", "d2v2")
        );

        Stream<List<Pair<String, String>>> combineResultStream = CombinationGenerator.generateExtWithNull(dimensionManager.getDimensions());
        List<MyConfig> myConfigs = combineResultStream.map(r -> new MyConfig(r)).collect(Collectors.toList());

        dimensionManager.createOverRelation(myConfigs, MyConfig::getDimensions, MyConfig::setSubConfigs);

        Assert.assertEquals(3 * 3, myConfigs.size());
        Map<String, MyConfig> configMap = ListUtils.toMap(myConfigs, MyConfig::getName);
        Assert.assertTrue(
                Arrays.asList(
                        "[[d1:null, d2:d2v1], [d1:1, d2:null], [d1:null, d2:null]]",
                        "[[d1:1, d2:null], [d1:null, d2:d2v1], [d1:null, d2:null]]").contains(
                        configMap.get("[d1:1, d2:d2v1]").subConfigs.toString()
                ));
    }

    @Getter
    @Setter
    public static class MyConfig {
        private String name;
        private List<Pair<String, String>> dimensions;
        private List<MyConfig> subConfigs = new ArrayList<>();

        MyConfig(List<Pair<String, String>> dimensions) {
            this.dimensions = dimensions;
            this.name = dimensions.toString();
        }

        @Override
        public String toString() {
            return name;
        }
    }

    @Test
    public void should_generate_permutation_with_dimension_name_success() {
        List<List<Pair<String, String>>> results = CombinationGenerator.generateExt(
                Pair.ofList("d1", "1", "2"),
                Pair.ofList("d2", "d2v1", "d2v2", null)
        ).collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(
                Arrays.asList(Pair.of("d1", "1"), Pair.of("d2", "d2v1")),
                Arrays.asList(Pair.of("d1", "2"), Pair.of("d2", "d2v1")),
                Arrays.asList(Pair.of("d1", "1"), Pair.of("d2", "d2v2")),
                Arrays.asList(Pair.of("d1", "2"), Pair.of("d2", "d2v2")),
                Arrays.asList(Pair.of("d1", "1"), Pair.of("d2", null)),
                Arrays.asList(Pair.of("d1", "2"), Pair.of("d2", null))
        ), results);
    }

    @Test
    public void should_generate_permutation_without_dimension_name_success() {
        List<List<String>> results = CombinationGenerator.generateSimple(
                Arrays.asList("1", "2"),
                Arrays.asList("d2v1", "d2v2", null)
        ).collect(Collectors.toList());
        Assert.assertEquals(Arrays.asList(
                Arrays.asList("1", "d2v1"),
                Arrays.asList("2", "d2v1"),
                Arrays.asList("1", "d2v2"),
                Arrays.asList("2", "d2v2"),
                Arrays.asList("1", null),
                Arrays.asList("2", null)
        ), results);
    }
}
