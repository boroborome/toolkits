package com.happy3w.toolkits.permutation;

import com.happy3w.toolkits.utils.IndexMapper;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DuplicatedPermutationMaker<T> {
    private IndexMapper<T> mapper;


    public DuplicatedPermutationMaker(T[] baseValues, BiPredicate<T, T> equalChecker) {
        mapper = new IndexMapper<>(baseValues, equalChecker);
    }

    public Stream<T[]> generate() {
        int[] startValues = mapper.createStartValues();
        return StreamSupport.stream(new PermutationSpliterator(startValues), false)
                .map(mapper::convertValues);
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
