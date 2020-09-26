package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class DimensionManager<K, V> {
    private final List<Pair<K, List<V>>> dimensions;
    private final Map<String, Integer> weightMaskMap = new HashMap<>();

    public DimensionManager(Pair<K, List<V>>... dimensions) {
        this.dimensions = Arrays.asList(dimensions);

        int weightMask = 1;
        for (Pair<K, List<V>> dimension : dimensions) {
            for (V value : dimension.getValue()) {
                String key = calculateWeightMaskKey(dimension.getKey(), value);
                weightMaskMap.put(key, weightMask);
                weightMask <<= 1;
            }
        }
    }

    private String calculateWeightMaskKey(K dimensionName, V dimensionValue) {
        return MessageFormat.format("{0}-{1}", dimensionName, dimensionValue);
    }

    public <C> void createOverRelation(
            List<C> configs,
            Function<C, List<Pair<K, V>>> dimensionGetter,
            BiConsumer<C, List<C>> subConfigSetter) {
        Map<C, CombineResult<K, V>> configDictionary = ListUtils.toMap(configs,
                c -> c,
                c -> wrapper(dimensionGetter.apply(c)));

        configDictionary.entrySet().forEach(entry -> {
            C config = entry.getKey();
            CombineResult<K, V> combineResult = entry.getValue();

            List<C> subConfigs = configDictionary.entrySet().stream()
                    .filter(otherEntry -> combineResult.isOver(otherEntry.getValue()))
                    .sorted(Comparator.comparing(e -> -e.getValue().getScore()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            subConfigSetter.accept(config, subConfigs);
        });
    }

    public CombineResult<K, V> wrapper(List<Pair<K, V>> simpleResult) {
        int mask = 0;
        int score = 0;
        for (Pair<K, V> dimensionValue : simpleResult) {
            if (dimensionValue.getValue() == null) {
                continue;
            }
            String weightMaskKey = calculateWeightMaskKey(dimensionValue.getKey(), dimensionValue.getValue());
            mask |= weightMaskMap.get(weightMaskKey);
            score++;
        }
        return new CombineResult<K, V>(simpleResult, score, mask);
    }
}
