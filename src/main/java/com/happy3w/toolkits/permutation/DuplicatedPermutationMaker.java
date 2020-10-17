package com.happy3w.toolkits.permutation;

import com.happy3w.toolkits.utils.ListUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DuplicatedPermutationMaker<T> {
    private int baseValueSize;
    private T[] metaValues;
    private int[] metaCounts;


    public DuplicatedPermutationMaker(T[] baseValues, BiPredicate<T, T> equalChecker) {
        int[] itemCountsTemp = new int[baseValues.length];
        Arrays.fill(itemCountsTemp, 1);
        List<T> collectValues = new ArrayList<>();
        for (T value : baseValues) {
            int index = ListUtils.indexOf(collectValues, value, equalChecker);
            if (index >= 0) {
                itemCountsTemp[index]++;
            } else {
                collectValues.add(value);
            }
        }

        this.baseValueSize = baseValues.length;
        this.metaValues = (T[]) Array.newInstance(baseValues.getClass().getComponentType(), collectValues.size());
        this.metaValues = collectValues.toArray(this.metaValues);
        this.metaCounts = Arrays.copyOf(itemCountsTemp, metaValues.length);
    }

    public Stream<T[]> generate() {
        int[] startValues = createStartValues(metaCounts);
        return StreamSupport.stream(new PermutationSpliterator(startValues), false)
                .map(this::convertValues);
    }

    private T[] convertValues(int[] indexes) {
        int size = indexes.length;
        T[] values = (T[]) Array.newInstance(metaValues.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            values[i] = metaValues[indexes[i]];
        }
        return values;
    }

    private int[] createStartValues(int[] itemCounts) {
        int[] startValues = new int[baseValueSize];
        for (int curIndex = 0, itemIndex = 0; itemIndex < itemCounts.length; itemIndex++) {
            int count = itemCounts[itemIndex];
            Arrays.fill(startValues, curIndex, curIndex + count, itemIndex);
            curIndex += count;
        }

        return startValues;
    }

    private static class PermutationSpliterator extends Spliterators.AbstractSpliterator<int[]> {
        private int[] startValue;
        private int[] currentValue;

        protected PermutationSpliterator(int[] startValue) {
            super(0, 0);
            this.startValue = startValue;
        }

        @Override
        public boolean tryAdvance(Consumer<? super int[]> action) {
            if (currentValue == null) {
                currentValue = startValue;
            } else {
                int[] nextValue = next(currentValue);
                if (nextValue == null) {
                    return false;
                }
                currentValue = nextValue;
            }
            action.accept(Arrays.copyOf(currentValue, currentValue.length));
            return true;
        }

        private int[] next(int[] values) {
            int indexSmallThenRight = lastIndexOfSmallThenRight(values);
            if (indexSmallThenRight < 0) {
                return null;
            }
            int indexMinBigValue = findMinBigValue(values, indexSmallThenRight);
            switchValue(values, indexSmallThenRight, indexMinBigValue);
            Arrays.sort(values, indexSmallThenRight + 1, values.length);
            return values;
        }

        private void switchValue(int[] values, int indexA, int indexB) {
            int a = values[indexA];
            values[indexA] = values[indexB];
            values[indexB] = a;
        }

        private int findMinBigValue(int[] values, int startIndex) {
            int curValue = values[startIndex];

            int minBigValueIndex = startIndex + 1;
            int minBigValue = values[minBigValueIndex];

            for (int index = startIndex + 2; index < values.length; ++index) {
                int value = values[index];
                if (value > curValue && value < minBigValue) {
                    minBigValueIndex = index;
                    minBigValue = value;
                }
            }
            return minBigValueIndex;
        }

        private int lastIndexOfSmallThenRight(int[] values) {
            int rightValue = values[values.length - 1];
            for (int index = values.length - 2; index >= 0; --index) {
                int curValue = values[index];
                if (curValue < rightValue) {
                    return index;
                }
                rightValue = curValue;
            }
            return -1;
        }
    }
}
