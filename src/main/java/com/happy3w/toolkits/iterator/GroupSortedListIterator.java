package com.happy3w.toolkits.iterator;

import com.happy3w.toolkits.utils.Pair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class GroupSortedListIterator<T, K, V> extends NeedFindIterator<Map.Entry<K, List<V>>> {
    protected final Iterator<T> innerIterator;
    protected final Function<T, K> keyGenerator;
    protected final Function<T, V> valueGenerator;

    protected boolean isInitialized = false;
    protected Pair<K, T> preItem;

    public GroupSortedListIterator(Iterator<T> innerIterator, Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        this.innerIterator = innerIterator;
        this.keyGenerator = keyGenerator;
        this.valueGenerator = valueGenerator;
    }

    @Override
    protected void findNext() {
        status = IteratorStatus.end;

        MapEntry<K, List<V>> entry = startToCollect();
        if (entry != null) {
            collectAllSameKey(entry);
            this.nextItem = entry;
            status = IteratorStatus.found;
        }
    }

    private void collectAllSameKey(MapEntry<K, List<V>> entry) {
        K currentKey = entry.getKey();
        List<V> valueList = entry.getValue();

        while (innerIterator.hasNext()) {
            T value = innerIterator.next();
            K key = keyGenerator.apply(value);

            if (Objects.equals(key, currentKey)) {
                valueList.add(valueGenerator.apply(value));
            } else {
                preItem = new Pair<>(key, value);
                break;
            }
        }
    }

    private MapEntry<K, List<V>> startToCollect() {
        if (!isInitialized) {
            isInitialized = true;
            if (innerIterator.hasNext()) {
                T value = innerIterator.next();
                K key = keyGenerator.apply(value);
                preItem = new Pair<>(key, value);
            }
        }

        if (preItem == null) {
            return null;
        }

        List<V> lst = new ArrayList<>();
        MapEntry<K, List<V>> entry = new MapEntry<>(preItem.getKey(), lst);
        lst.add(valueGenerator.apply(preItem.getValue()));
        preItem = null;
        return entry;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MapEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        @Override
        public V setValue(V value) {
            V origin = this.value;
            this.value = value;
            return origin;
        }
    }
}
