package com.happy3w.toolkits.utils;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Pair<K, V> {

    private K key;

    private V value;

    public String toString() {
        return MessageFormat.format("{0}:{1}", key, value);
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    public static <K, V> Pair<K, List<V>> ofList(K k, V... vs) {
        return new Pair<>(k, Arrays.asList(vs));
    }
}

