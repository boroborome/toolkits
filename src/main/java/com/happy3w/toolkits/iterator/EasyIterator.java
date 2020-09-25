package com.happy3w.toolkits.iterator;

import com.happy3w.toolkits.utils.MapUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class EasyIterator<T> implements Iterator<T> {

    public static <T> EasyIterator<T> from(Stream<T> stream) {
        return new IteratorExtIterator<>(stream.iterator());
    }

    public static <T> EasyIterator<T> from(Iterator<T> iterator) {
        return new IteratorExtIterator<>(iterator);
    }

    public static <T> EasyIterator<T> from(T... values) {
        return new IteratorExtIterator<>(Arrays.asList(values).iterator());
    }

    public static IntRangeIterator range(int start, int end) {
        return new IntRangeIterator(start, end);
    }

    public EasyIterator<List<T>> split(int size) {
        return new SplitIterator<T>(this, size);
    }

    public <R> EasyIterator<R> map(Function<T, R> mapMethod) {
        return new MapIterator(this, mapMethod);
    }

    public EasyIterator<T> peek(Consumer<T> consumeMethod) {
        return new PeekIterator<>(this, consumeMethod);
    }

    public <R> EasyIterator<R> flatMap(Function<T, Iterator<R>> mapMethod) {
        return new FlatMapIterator(this, mapMethod);
    }

    public EasyIterator<T> filter(Predicate<T> predicate) {
        return new FilterIterator<T>(this, predicate);
    }

/**
 * 按照Key分组
 * 前一个Iterator必须是已经根据key排序好的
 * @param keyGenerator key的获取方法
 * @param <K> key的类型
 * @return 按照key分组后的Entity迭代器
 */
public <K> EasyIterator<Map.Entry<K, List<T>>> groupBy(Function<T, K> keyGenerator) {
    return new GroupSortedListIterator<>(this, keyGenerator, v -> v);
}

    public <K, V> EasyIterator<Map.Entry<K, List<V>>> groupBy(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        return new GroupSortedListIterator<>(this, keyGenerator, valueGenerator);
    }

    public void forEach(Consumer<T> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }

    public void forEach(BiConsumer<T, Integer> consumer) {
        for (int index = 0; hasNext(); index++) {
            consumer.accept(next(), index);
        }
    }

    public <K> Map<K, T> toMap(Function<T, K> keyGenerator) {
        return toMap(keyGenerator, v -> v);
    }

    public <K, V> Map<K, V> toMap(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, V> map = new HashMap<>();
        while (hasNext()) {
            T t = next();
            K k = keyGenerator.apply(t);
            V v = valueGenerator.apply(t);
            map.put(k, v);
        }
        return map;
    }

    public <K> Map<K, List<T>> toMapList(Function<T, K> keyGenerator) {
        return toMapList(keyGenerator, v -> v);
    }

    public <K, V> Map<K, List<V>> toMapList(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, List<V>> map = new HashMap<>();
        while (hasNext()) {
            T t = next();
            K k = keyGenerator.apply(t);
            V v = valueGenerator.apply(t);
            MapUtils.safeRead(map, k, () -> new ArrayList<>())
                    .add(v);
        }
        return map;
    }

    public List<T> toList() {
        List<T> values = new ArrayList<>();
        while (hasNext()) {
            values.add(next());
        }
        return values;
    }
}
