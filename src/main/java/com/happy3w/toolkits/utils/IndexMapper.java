package com.happy3w.toolkits.utils;

import lombok.Getter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiPredicate;

/**
 * Used to convert values to index map<br>
 *     like this:
 *     IndexMapper mapper = IndexMapper(new String[]{"a", "b", "a", "c", "b"}, (a, b) -&gt; a.equals(b));
 *     then
 *     mapper.baseValueSize == 5, it is the length of input values.
 *     mapper.metaValues == ["a", "b", "c"], it is the different values.
 *     mapper.metaCounts == [2, 2, 1], it is the count of each metaValue. 2a,2b and 1c
 * @param <T> The value type
 */
@Getter
public class IndexMapper<T> {
    /**
     * The length of input values.
     */
    private int baseValueSize;

    /**
     * The different values.
     */
    private T[] metaValues;

    /**
     * The count of each metaValue
     */
    private int[] metaCounts;

    /**
     * Constructor
     * @param baseValues Some values, There maybe contains repeat values
     * @param equalChecker How to check two value is equal
     */
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
