package com.happy3w.toolkits.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class MapUtils {
    public static <ValueType, KeyType> Map<ValueType, KeyType> swapToValueKey(Map<KeyType, ValueType> map) {
        Map<ValueType, KeyType> resultMap = new HashMap<>();

        map.forEach((k, v) -> resultMap.put(v, k));

        return resultMap;
    }

    @Deprecated // Please use Map.computeIfAbsent
    public static <K, V> V safeRead(Map<K, V> map, K key, Supplier<V> supplier) {
        V value = map.get(key);
        if (value == null) {
            value = supplier.get();
            map.put(key, value);
        }
        return value;
    }

    public static <V> V safeRead(Map<?, ?> map, Supplier<V> supplierLast, Object... otherKeys) {
        List<MapKey> mapKeys = new ArrayList<>();
        if (otherKeys == null || otherKeys.length <= 0) {
            throw new IllegalArgumentException("At lest has one key.");
        } else {
            for (int i = 0; i < otherKeys.length - 1; i++) {
                Object key = otherKeys[i];
                mapKeys.add(MapKey.of(key, () -> new HashMap()));
            }
            mapKeys.add(MapKey.of(otherKeys[otherKeys.length - 1], supplierLast));
        }
        return (V) safeRead(map, mapKeys.toArray(new MapKey[otherKeys.length]));
    }

    public static <K, V> V safeRead(Map<K, V> map, MapKey... mapKeys) {
        Object v = map;
        for (MapKey mapKey : mapKeys) {
            Map m = (Map) v;
            v = safeRead(m, mapKey.key, mapKey.supplier);
        }
        return (V) v;
    }

    @Getter
    @AllArgsConstructor
    public static class MapKey<K, V> {
        private K key;
        private Supplier<V> supplier;

        public static <K, V> MapKey<K, V> of(K key, Supplier<V> supplier) {
            return new MapKey<>(key, supplier);
        }
    }

    public static <T> T findByType(Class key, Map<Class, T> map) {
        T t = map.get(key);
        if (t != null) {
            return t;
        }

        for (Map.Entry<Class, T> entry : map.entrySet()) {
            if (entry.getKey().isAssignableFrom(key)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
