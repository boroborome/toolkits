package com.happy3w.toolkits.combination;

import com.happy3w.toolkits.utils.IndexMapper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class GroupCombinationMaker<T> {
    private IndexMapper<T> mapper;

    public GroupCombinationMaker(T[] baseValues, BiPredicate<T, T> equalChecker) {
        mapper = new IndexMapper<>(baseValues, equalChecker);
    }

    public Stream<T[][]> makeByItemCounts(int[] itemCountsInGroup) {
        int[] startValues = mapper.createStartValues();
        return StreamSupport.stream(new GroupCombineSpliterator(startValues, itemCountsInGroup), false)
                .map(this::convertValues);
    }

//    public Stream<T[][]> makeByGroupCount(int groupCount) {
//        return GroupMaker.make(mapper.getBaseValueSize(), groupCount)
//                .flatMap(this::makeByItemCounts);
//    }

    private T[][] convertValues(int[][] combineResult) {
        T[][] result = (T[][]) Array.newInstance(mapper.getMetaValues().getClass(), combineResult.length);
        for (int i = 0; i < combineResult.length; ++i) {
            result[i] = mapper.convertValues(combineResult[i]);
        }
        return result;
    }

    private static class GroupCombineSpliterator extends Spliterators.AbstractSpliterator<int[][]> {
        private int[] groupMap;
        private int[] startValue;
        private GroupItem[] groupItems;
        private int[] currentValue;

        protected GroupCombineSpliterator(int[] startValue, int[] itemCountsInGroup) {
            super(0, 0);
            this.startValue = startValue;
            this.groupMap = new int[startValue.length];
            this.groupItems = new GroupItem[itemCountsInGroup.length];
            for (int i = 0, startIndex = 0; i < groupItems.length; i++) {
                int count = itemCountsInGroup[i];
                Arrays.fill(groupMap, startIndex, startIndex + count, i);
                groupItems[i] = new GroupItem(startIndex, count);
                startIndex += count;
            }
        }

        @Override
        public boolean tryAdvance(Consumer<? super int[][]> action) {
            if (currentValue == null) {
                currentValue = startValue;
            } else {
                int[] nextValue = next(currentValue);
                if (nextValue == null) {
                    return false;
                }
                currentValue = nextValue;
            }

            int[][] result = convertResult(currentValue, groupItems);
            action.accept(result);
            return true;
        }

        private int[][] convertResult(int[] values, GroupItem[] groupItems) {
            int[][] result = new int[groupItems.length][];
            for (int i = 0; i < groupItems.length; ++i) {
                GroupItem item = groupItems[i];
                result[i] = Arrays.copyOfRange(values, item.startIndex, item.startIndex + item.count);
            }
            return result;
        }

        private int[] next(int[] values) {
            while (true) {
                values = tryFindNext(values);
                if (values == null) {
                    return null;
                }
                if (duplicateResult(groupItems)) {
                    continue;
                }
                return values;
            }
        }

        private int[] tryFindNext(int[] values) {
            for (int index = values.length -2; index >= 0; --index) {
                int indexMinBigValue = findMinBigValue(values, index);
                if (indexMinBigValue < 0) {
                    continue;
                }
                switchValue(values, index, indexMinBigValue);
                sortPart(values, index, indexMinBigValue);
                updateGroupWeight(values, groupMap[index]);
                return values;
            }
            return null;
        }

        private boolean duplicateResult(GroupItem[] groupItems) {
            for (int curIndex = groupItems.length - 1; curIndex > 0; --curIndex) {
                GroupItem curItem = groupItems[curIndex];
                for (int checkIndex = curIndex - 1; checkIndex >= 0; -- checkIndex) {
                    GroupItem checkItem = groupItems[checkIndex];
                    if (curItem.count == checkItem.count && curItem.weight < checkItem.weight) {
                        return true;
                    }
                }
            }
            return false;
        }

        private void updateGroupWeight(int[] values, int startGroup) {
            for (int i = startGroup; i < groupItems.length; ++i) {
                groupItems[i].updateWeight(values);
            }
        }

        private void sortPart(int[] values, int startIndex, int exceptIndex) {
            int curGroupIndex = groupMap[startIndex];
            GroupItem curGroupItem = groupItems[curGroupIndex];

            for (int index = startIndex; index < curGroupItem.endIndex - 1; ++index) {
                int indexMinBigValue = findMinBigValue(values, index);
                if (indexMinBigValue < 0 || indexMinBigValue == index + 1) {
                    continue;
                }
                switchValue(values, index + 1, indexMinBigValue);
            }
            Arrays.sort(values, curGroupItem.endIndex, values.length);
        }

        private void switchValue(int[] values, int indexA, int indexB) {
            int a = values[indexA];
            values[indexA] = values[indexB];
            values[indexB] = a;
        }

        private int findMinBigValue(int[] values, int startIndex) {
            int curValue = values[startIndex];
            int curGroup = groupMap[startIndex];

            int minBigValueIndex = -1;
            int minBigValue = Integer.MAX_VALUE;

            for (int index = startIndex + 1; index < values.length; ++index) {
                int group = groupMap[index];
                if (group == curGroup) {
                    continue;
                }
                int value = values[index];
                if (value > curValue && value < minBigValue) {
                    minBigValueIndex = index;
                    minBigValue = value;
                }
            }
            return minBigValueIndex;
        }
    }

    private static class GroupItem {
        private int startIndex;
        private int endIndex;
        private int count;
        private long weight;

        public GroupItem(int startIndex, int count) {
            this.startIndex = startIndex;
            this.count = count;
            this.endIndex = startIndex + count;
        }

        public void updateWeight(int[] values) {
            long weight = 0;
            for (int i = startIndex; i < endIndex; ++i) {
                weight |= (1l << values[i]);
            }
            this.weight = weight;
        }
    }
}
