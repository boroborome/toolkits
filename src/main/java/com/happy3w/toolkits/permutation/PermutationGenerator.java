package com.happy3w.toolkits.permutation;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class PermutationGenerator<T> {
    private T[] baseValues;

    private int[] factorial;

    public PermutationGenerator(T[] baseValues) {
        this.baseValues = baseValues;
    }

    public Stream<T[]> generate() {
        if (factorial == null) {
            factorial = initFactorial(baseValues.length);
        }
        return StreamSupport.stream(new PermutationSpliterator(baseValues.length, factorial), false)
                .map(this::convertValues);
    }

    private T[] convertValues(int[] indexes) {
        int size = baseValues.length;;
        T[] values = (T[]) Array.newInstance(baseValues.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            values[i] = baseValues[indexes[i]];
        }
        return values;
    }

    private int[] initFactorial(int length) {
        int[] factorial = new int[length + 1];
        factorial[0] = 1;
        for (int i = 1; i <= length; i++) {
            factorial[i] = factorial[i - 1] * i;
        }

        return factorial;
    }

    private static class PermutationSpliterator extends Spliterators.AbstractSpliterator<int[]> {
        private int[] factorial;
        private int size;
        private int index = 0;

        protected PermutationSpliterator(int size, int[] factorial) {
            super(0, 0);
            this.factorial = factorial;
            this.size = size;
        }

        @Override
        public boolean tryAdvance(Consumer<? super int[]> action) {
            if (index >= factorial[size]) {
                return false;
            }

            int[] newPermutation = createPermutationByIndex(index);
            action.accept(newPermutation);

            index++;
            return true;
        }

        private int[] createPermutationByIndex(int leftIndex) {
            int[] newPermutation = new int[size];
            int[] valid = new int[size];
            Arrays.fill(valid, 1);
            for (int i = 0; i < size; ++i) {
                int curFactorial = factorial[size - i - 1];
                int order = leftIndex / curFactorial + 1;
                for (int j = 0; j < size; ++j) {
                    order -= valid[j];
                    if (order == 0) {
                        newPermutation[i] = j;
                        valid[j] = 0;
                        break;
                    }
                }
                leftIndex %= curFactorial;
            }
            return newPermutation;
        }
    }
}
