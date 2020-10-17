package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.MapUtils;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DimCombinationMakerTest {

    @Test
    public void should_used_in_composite_scene_success() {
        List<DimCombineDetail<String, String>> details = DimCombinationMaker.<String, String>builder()
                .dimension("d1", "1", "2")
                .dimension("d2", "d2v1", "d2v2")
                .withNullDimension(true)
                .withOverRelation(true)
                .build()
                .generateDetail()
                .collect(Collectors.toList());

        Map<String, MyConfig> configMap = new HashMap<>();
        List<MyConfig> myConfigs = details.stream()
                .map(d -> createConfig(d, configMap))
                .collect(Collectors.toList());

        Assert.assertEquals(3 * 3, myConfigs.size());
        Assert.assertTrue(
                Arrays.asList(
                        "[[d1:null, d2:d2v1], [d1:1, d2:null], [d1:null, d2:null]]",
                        "[[d1:1, d2:null], [d1:null, d2:d2v1], [d1:null, d2:null]]").contains(
                        configMap.get("[d1:1, d2:d2v1]").subConfigs.toString()
                ));
    }

    private MyConfig createConfig(DimCombineDetail<String, String> detail, Map<String, MyConfig> configMap) {
        String key = detail.getNormalResult().toString();
        MyConfig config = MapUtils.safeRead(configMap, key, () -> new MyConfig(detail.getNormalResult()));
        if (detail.getOveredCombines() == null) {
            return config;
        }
        for (DimCombineDetail<String, String> overedDetail : detail.getOveredCombines()) {
            String newKey = overedDetail.getNormalResult().toString();
            config.getSubConfigs().add(MapUtils.safeRead(configMap, newKey, () -> new MyConfig(overedDetail.getNormalResult())));
        }
        return config;
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
        List<List<Pair<String, String>>> results = DimCombinationMaker.<String, String>builder()
                .dimension("d1", "1", "2")
                .dimension("d2", "d2v1", "d2v2", null)
                .build()
                .generateNormal()
                .collect(Collectors.toList());
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
        List<List<String>> results = DimCombinationMaker.<String, String>builder()
                .dimension("d1", "1", "2")
                .dimension("d2","d2v1", "d2v2", null)
                .build()
                .generateSimple()
                .collect(Collectors.toList());
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
