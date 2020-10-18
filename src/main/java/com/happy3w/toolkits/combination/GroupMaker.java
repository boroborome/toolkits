package com.happy3w.toolkits.combination;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GroupMaker {
    public static Stream<int[]> make(int dataSize, int groupSize, boolean ignoreOrder) {
        return StreamSupport.stream(new GroupSpliterator(dataSize, groupSize, ignoreOrder), false);
    }

    public static class GroupSpliterator extends Spliterators.AbstractSpliterator<int[]> {
        private int dataSize;
        private int groupSize;
        private boolean ignoreOrder;
        private int[] currentValue;

        protected GroupSpliterator(int dataSize, int groupSize, boolean ignoreOrder) {
            super(0, 0);
            this.dataSize = dataSize;
            this.groupSize = groupSize;
            this.ignoreOrder = ignoreOrder;
        }

        @Override
        public boolean tryAdvance(Consumer<? super int[]> action) {
            if (currentValue == null) {
                currentValue = initValue();
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
            for (int digIndex = values.length - 1; digIndex > 0; --digIndex) {
                int digValue = values[digIndex];
                if (digValue == 1) {
                    continue;
                }
                values[digIndex - 1]++;
                values[digIndex] = 1;
                values[values.length - 1] += (digValue - 2);
                return values;
            }
            return null;
        }

        private int[] initValue() {
            int[] newValue = new int[this.groupSize];
            Arrays.fill(newValue, 1);
            newValue[groupSize - 1] = dataSize - groupSize + 1;
            return newValue;
        }
    }
}
