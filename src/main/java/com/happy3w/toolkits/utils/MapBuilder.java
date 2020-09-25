package com.happy3w.toolkits.utils;


import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MapBuilder<K, V> {
    private Map<K, V> map = new HashMap<>();

    public MapBuilder() {
    }

    public MapBuilder(K key, V value) {
        and(key, value);
    }

    public static <A, B> MapBuilder<A, B> of(A key, B value) {
        return new MapBuilder<>(key, value);
    }

    public MapBuilder<K, V> and(K key, V value) {
        map.put(key, value);
        return this;
    }

    public Map<K, V> build() {
        return Collections.unmodifiableMap(map);
    }

    public Map<K, V> buildNormal() {
        return map;
    }
}
