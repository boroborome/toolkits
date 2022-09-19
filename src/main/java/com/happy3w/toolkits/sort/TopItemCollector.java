package com.happy3w.toolkits.sort;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class TopItemCollector<T> {
    private final Object[] items;
    private final Function<T, Integer> orderValueGenerator;

    public TopItemCollector(int topSize,
                            Function<T, Integer> orderValueGenerator) {
        this.items = new Object[topSize];
        this.orderValueGenerator = orderValueGenerator;
    }

    public Stream<T> collectedItems() {
        return Stream.of(items)
                .filter(Objects::nonNull)
                .map(v -> (T) v);
    }

    public boolean accept(T newItem) {
        Integer curItemValue = orderValueGenerator.apply(newItem);
        for (int i = 0; i < items.length; i++) {
            T item = (T) items[i];
            if (compareItem(curItemValue, item) > 0) {
                moveDown(i);
            } else {
                return saveInfo(i - 1, newItem);
            }
        }
        return  saveInfo(items.length - 1, newItem);
    }

    private boolean saveInfo(int targetIndex, T info) {
        if (targetIndex < 0) {
            return false;
        }
        items[targetIndex] = info;
        return true;
    }

    private void moveDown(int index) {
        int targetIndex = index - 1;
        if (targetIndex < 0) {
            return;
        }
        items[targetIndex] = items[index];
    }

    private int compareItem(int curItemValue, T item) {
        return item == null ? 1 : curItemValue - orderValueGenerator.apply(item);
    }
}
