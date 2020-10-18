package com.happy3w.toolkits.combination;

import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GroupMaker {
    public static Stream<int[]> make(int dataSize, int groupSize) {
        return StreamSupport.stream(new GroupSpliterator(dataSize, groupSize), false);
    }

    public static class GroupSpliterator extends Spliterators.AbstractSpliterator<int[]> {
        private int dataSize;
        private int groupSize;
        private int[] currentValue;

        protected GroupSpliterator(int dataSize, int groupSize) {
            super(0, 0);
            this.dataSize = dataSize;
            this.groupSize = groupSize;
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
            int locInc = findIncreaseAbleLocation(values);
            if (locInc < 0) {
                return null;
            }

            int standardValue = values[locInc] + 1;
            int remainValue = 0;
            for (int index = values.length - 2; index >= locInc; --index) {
                remainValue += (values[index] - standardValue);
                values[index] = standardValue;
            }

            values[values.length - 1] += remainValue;
            return values;
        }

        private int findIncreaseAbleLocation(int[] values) {
            int lastValue = values[values.length - 1];

            for (int digIndex = values.length - 2; digIndex >= 0; --digIndex) {
                int digValue = values[digIndex];
                if (digValue + 2 <= lastValue) {
                    return digIndex;
                }
            }
            return -1;
        }

        private int[] initValue() {
            int[] newValue = new int[this.groupSize];
            Arrays.fill(newValue, 1);
            newValue[groupSize - 1] = dataSize - groupSize + 1;
            return newValue;
        }
    }
}
