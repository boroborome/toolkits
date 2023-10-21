package com.happy3w.toolkits.iterator;

import java.util.*;
import java.util.function.Function;

public class GroupByIterator {

    public static <T, K> IEasyIterator<Map.Entry<K, List<T>>> groupBy(
            Iterator<T> it,
            Function<T, K> keyGenerator) {
        return groupBy(it, keyGenerator, Function.identity());
    }

    public static <T, K, V> IEasyIterator<Map.Entry<K, List<V>>> groupBy(
            Iterator<T> it,
            Function<T, K> keyGenerator,
            Function<T, V> valueGenerator) {
        Map<K, List<V>> map = new HashMap<>();
        while (it.hasNext()) {
            T t = it.next();
            K key = keyGenerator.apply(t);
            V value = valueGenerator.apply(t);

            List<V> ls = map.computeIfAbsent(key, k -> new ArrayList<>());
            ls.add(value);
        }
        return EasyIterator.fromIterable(map.entrySet());
    }
}
