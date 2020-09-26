package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Used to combine multi dimension, generate all permutation and combination
 */
public class CombinationGenerator {
    /**
     * Generate all permutation and combination with dimensions
     *
     * @param dimensions The key is dimension name. The value is the dimension value range. It accept null as legal dimension value.There should not contains same value in value range, but this method does not check it.
     * @param <K>        Dimension name type
     * @param <V>        Dimension value type
     * @return Stream of permutation, each result is a list of dimension name and value.
     */
    public static <K, V> Stream<List<Pair<K, V>>> generateExt(Pair<K, List<V>>... dimensions) {
        return generateExt(Arrays.asList(dimensions));
    }

    public static <K, V> Stream<List<Pair<K, V>>> generateExt(List<Pair<K, List<V>>> dimensions) {
        return StreamSupport.stream(CombineSpliterator.of(dimensions), false);
    }

    /**
     * Generate all permutation and combination with dimensions. This method will auto add null to every dimension.
     *
     * @param dimensions The key is dimension name. The value is the dimension value range. <b>Do not add null to value range. This method will auto do it.</b>
     * @param <K>        Dimension name type
     * @param <V>        Dimension value type
     * @return Stream of permutation, each result is a list of dimension name and value.
     */
    public static <K, V> Stream<List<Pair<K, V>>> generateExtWithNull(Pair<K, List<V>>... dimensions) {
        return generateExtWithNull(Arrays.asList(dimensions));
    }

    public static <K, V> Stream<List<Pair<K, V>>> generateExtWithNull(List<Pair<K, List<V>>> dimensions) {
        return generateExt(dimensions.stream().map(dimensionItem -> {
            List<V> newDimValues = new ArrayList<>(dimensionItem.getValue());
            newDimValues.add(null);
            return new Pair<>(dimensionItem.getKey(), newDimValues);
        }).collect(Collectors.toList()));
    }

    /**
     * Generate all permutation and combination with dimensions.
     *
     * @param dimensions Only the dimension values
     * @param <V>        Dimension value type
     * @return Stream of permutation, each result is a list of dimension name and value.
     */
    public static <V> Stream<List<V>> generateSimple(List<V>... dimensions) {
        return generateExt(Arrays.asList(dimensions).stream()
                .map(dvs -> new Pair<>("", dvs))
                .collect(Collectors.toList())
        ).map(extValue -> extValue.stream()
                .map(pair -> pair.getValue())
                .collect(Collectors.toList())
        );
    }

    /**
     * Generate all permutation and combination with dimensions. This method will auto add null to every dimension.
     *
     * @param dimensions The key is dimension name. The value is the dimension value range. <b>Do not add null to value range. This method will auto do it.</b>
     * @param <V>        Dimension value type
     * @return Stream of permutation, each result is a list of dimension name and value.
     */
    public static <V> Stream<List<V>> generateSimpleWithNull(List<V>... dimensions) {
        return generateExtWithNull(Arrays.asList(dimensions).stream()
                .map(dvs -> new Pair<>("", dvs))
                .collect(Collectors.toList())
        ).map(extValue -> extValue.stream()
                .map(pair -> pair.getValue())
                .collect(Collectors.toList())
        );
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
}
