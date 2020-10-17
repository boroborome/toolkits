package com.happy3w.toolkits.utils;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

@Getter
public class IndexMapper<T> {
    private int baseValueSize;
    private T[] metaValues;
    private int[] metaCounts;

    public IndexMapper(T[] baseValues, BiPredicate<T, T> equalChecker) {
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

    public T[] convertValues(int[] indexes) {
        int size = indexes.length;
        T[] values = (T[]) Array.newInstance(metaValues.getClass().getComponentType(), size);
        for (int i = 0; i < size; i++) {
            values[i] = metaValues[indexes[i]];
        }
        return values;
    }

    public int[] createStartValues() {
        int[] startValues = new int[baseValueSize];
        for (int curIndex = 0, itemIndex = 0; itemIndex < metaCounts.length; itemIndex++) {
            int count = metaCounts[itemIndex];
            Arrays.fill(startValues, curIndex, curIndex + count, itemIndex);
            curIndex += count;
        }

        return startValues;
    }
}
