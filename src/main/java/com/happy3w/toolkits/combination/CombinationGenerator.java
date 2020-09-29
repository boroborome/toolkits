package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.ListUtils;
import com.happy3w.toolkits.utils.Pair;
import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Used to combine multi dimension, generate all permutation and combination
 */
@Getter
@Setter
public class CombinationGenerator<K, V> {
    private List<Pair<K, List<V>>> dimensions = new ArrayList<>();
    private boolean withNullDimension = false;
    private boolean withOverRelation = false;
    private final Map<String, Integer> weightMaskMap = new HashMap<>();

    private void initWeightMask() {
        int weightMask = 1;
        for (Pair<K, List<V>> dimension : dimensions) {
            for (V value : dimension.getValue()) {
                String key = genWeightMaskKey(dimension.getKey(), value);
                weightMaskMap.put(key, weightMask);
                weightMask <<= 1;
            }
        }
    }

    private String genWeightMaskKey(K dimensionName, V dimensionValue) {
        return MessageFormat.format("{0}-{1}", dimensionName, dimensionValue);
    }

    /**
     * Generate combinations
     * @return Stream of permutation, the score and mask is not initialized when withOverRelation=false
     */
    public Stream<CombineDetail<K, V>> generateDetail() {
        Stream<CombineDetail<K, V>> detailStream = generateNormal()
                .map(r -> new CombineDetail(r));
        if (withOverRelation) {
            return initOverRelation(detailStream);
        }
        return detailStream;
    }

    /**
     * Generate combinations
     * @return Stream of permutation, each result is a list of dimension name and value.
     */
    public Stream<List<Pair<K, V>>> generateNormal() {
        List<Pair<K, List<V>>> dimensions = mayBeNullDimensions(this.dimensions, withNullDimension);
        return StreamSupport.stream(CombineSpliterator.of(dimensions), false);
    }

    /**
     * Generate combinations
     * @return The result is only the value list, such as [['a1', 'b1'], ['a1', 'b2']...]
     */
    public Stream<List<V>> generateSimple() {
        return generateNormal().map(r -> ListUtils.map(r, Pair::getValue));
    }

    private List<Pair<K, List<V>>> mayBeNullDimensions(List<Pair<K, List<V>>> dimensions, boolean withNullDimension) {
        if (withNullDimension) {
            return dimensions.stream().map(dimensionItem -> {
                List<V> newDimValues = new ArrayList<>(dimensionItem.getValue());
                newDimValues.add(null);
                return new Pair<>(dimensionItem.getKey(), newDimValues);
            }).collect(Collectors.toList());
        }
        return dimensions;
    }

    private Stream<CombineDetail<K, V>> initOverRelation(Stream<CombineDetail<K, V>> detailStream) {
        List<CombineDetail<K, V>> detailList = detailStream.peek(this::initScoreAndMask)
                .collect(Collectors.toList());
        for (CombineDetail<K, V> curDetail : detailList) {
            for (CombineDetail<K, V> overedDetail : detailList) {
                if (curDetail.isOver(overedDetail)) {
                    curDetail.addOveredCombine(overedDetail);
                }
            }
        }
        return detailList.stream();
    }

    public void initScoreAndMask(CombineDetail<K, V> combineDetail) {
        int mask = 0;
        int score = 0;
        for (Pair<K, V> dimensionValue : combineDetail.getNormalResult()) {
            if (dimensionValue.getValue() == null) {
                continue;
            }
            String weightMaskKey = genWeightMaskKey(dimensionValue.getKey(), dimensionValue.getValue());
            mask |= weightMaskMap.get(weightMaskKey);
            score++;
        }

        combineDetail.setScore(score);
        combineDetail.setMask(mask);
    }

    private static class CombineSpliterator<K, V> extends Spliterators.AbstractSpliterator<List<Pair<K, V>>> {
        private List<Pair<K, List<V>>> dimensions;

        private int currentIndex = 0;

        protected CombineSpliterator(List<Pair<K, List<V>>> dimensions, long est) {
            super(est, Spliterator.SIZED);
            this.dimensions = dimensions;
        }

        @Override
        public boolean tryAdvance(Consumer<? super List<Pair<K, V>>> action) {
            if (currentIndex >= this.estimateSize()) {
                return false;
            }

            List<Pair<K, V>> currentDimension = createDimension(currentIndex);
            action.accept(currentDimension);
            currentIndex++;
            return true;
        }

        private List<Pair<K, V>> createDimension(int currentIndex) {
            List<Pair<K, V>> dimensionValueList = new ArrayList<>();

            int dimensionValue = currentIndex;
            for (Pair<K, List<V>> dimDef : dimensions) {
                int index = dimensionValue % dimDef.getValue().size();
                Pair<K, V> newItem = new Pair<>(dimDef.getKey(), dimDef.getValue().get(index));
                dimensionValueList.add(newItem);
                dimensionValue /= dimDef.getValue().size();
            }
            return dimensionValueList;
        }

        public static <K, V> CombineSpliterator<K, V> of(List<Pair<K, List<V>>> dimensions) {
            int maxSize = calculateMaxSize(dimensions);
            return new CombineSpliterator(dimensions, maxSize);
        }

        private static <V, K> int calculateMaxSize(List<Pair<K, List<V>>> dimensions) {
            int size = 1;
            for (Pair<K, List<V>> dimension : dimensions) {
                size *= dimension.getValue().size();
            }
            return size;
        }
    }

    public static <K, V> CombinationGeneratorBuilder<K, V> builder() {
        return new CombinationGeneratorBuilder<K, V>();
    }

    public static class CombinationGeneratorBuilder<K, V> {
        private CombinationGenerator<K, V> generator = new CombinationGenerator<>();

        /**
         * Add a dimension
         * @param dimensionName Dimension name, Must not be null
         * @param values dimension values
         * @return This builder
         */
        public CombinationGeneratorBuilder<K, V> dimension(K dimensionName, V... values) {
            if (dimensionName == null) {
                throw new UnsupportedOperationException("dimensionName can not be null.");
            }
            generator.dimensions.add(Pair.ofList(dimensionName, values));
            return this;
        }

        /**
         * Add some dimensions
         * @param dimensions The key is dimension name. The value is the dimension value range. It accept null as legal dimension value.There should not contains same value in value range, but this method does not check it.
         * @return This builder
         */
        public CombinationGeneratorBuilder<K, V> dimensions(List<Pair<K, List<V>>> dimensions) {
            generator.dimensions.addAll(dimensions);
            return this;
        }

        /**
         * Config generator whether auto add null to dimension when generate Combine Result.
         * @param withNull true means add null to result
         * @return This builder
         */
        public CombinationGeneratorBuilder<K, V> withNullDimension(boolean withNull) {
            generator.withNullDimension = withNull;
            return this;
        }

        /**
         * Config generator whether init CombineDetail.overCombines
         * @param withOverRelation true means init CombineDetail.overCombines
         * @return This builder
         */
        public CombinationGeneratorBuilder<K, V> withOverRelation(boolean withOverRelation) {
            generator.withOverRelation = withOverRelation;
            return this;
        }

        public CombinationGenerator<K, V> build() {
            generator.initWeightMask();
            return generator;
        }
    }
}
