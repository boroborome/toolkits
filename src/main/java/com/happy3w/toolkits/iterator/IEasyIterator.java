package com.happy3w.toolkits.iterator;

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
import java.util.stream.StreamSupport;

public interface IEasyIterator<T> extends Iterator<T> {

    default IEasyIterator<List<T>> split(int size) {
        return new SplitIterator<T>(this, size);
    }

    default <R> IEasyIterator<R> map(Function<T, R> mapMethod) {
        return new MapIterator(this, mapMethod);
    }

    default IEasyIterator<T> peek(Consumer<T> consumeMethod) {
        return new PeekIterator<>(this, consumeMethod);
    }

    default <R> IEasyIterator<R> flatMap(Function<T, Iterator<R>> mapMethod) {
        return new FlatMapIterator(this, mapMethod);
    }

    default IEasyIterator<T> filter(Predicate<T> predicate) {
        return new FilterIterator<T>(this, predicate);
    }

    /**
     * 按照Key分组
     * 前一个Iterator必须是已经根据key排序好的
     * @param keyGenerator key的获取方法
     * @param <K> key的类型
     * @return 按照key分组后的Entity迭代器
     */
    default <K> IEasyIterator<Map.Entry<K, List<T>>> groupBy(Function<T, K> keyGenerator) {
        return new GroupSortedListIterator<>(this, keyGenerator, v -> v);
    }

    default <K, V> IEasyIterator<Map.Entry<K, List<V>>> groupBy(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        return new GroupSortedListIterator<>(this, keyGenerator, valueGenerator);
    }

    default void forEach(Consumer<T> consumer) {
        while (hasNext()) {
            consumer.accept(next());
        }
    }

    default void forEach(BiConsumer<T, Integer> consumer) {
        for (int index = 0; hasNext(); index++) {
            consumer.accept(next(), index);
        }
    }

    default IEasyIterator<T> concat(IEasyIterator<T>... its) {
        List<IEasyIterator<T>> newIts = new ArrayList<>();
        newIts.add(this);
        newIts.addAll(Arrays.asList(its));
        return EasyIterator.from(newIts.iterator())
                .flatMap(it -> it);
    }

    default <K> Map<K, T> toMap(Function<T, K> keyGenerator) {
        return toMap(keyGenerator, v -> v);
    }

    default <K, V> Map<K, V> toMap(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, V> map = new HashMap<>();
        while (hasNext()) {
            T t = next();
            K k = keyGenerator.apply(t);
            V v = valueGenerator.apply(t);
            map.put(k, v);
        }
        return map;
    }

    default <K> Map<K, List<T>> toMapList(Function<T, K> keyGenerator) {
        return toMapList(keyGenerator, v -> v);
    }

    default <K, V> Map<K, List<V>> toMapList(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        Map<K, List<V>> map = new HashMap<>();
        while (hasNext()) {
            T t = next();
            K k = keyGenerator.apply(t);
            V v = valueGenerator.apply(t);
            map.computeIfAbsent(k, $ -> new ArrayList<>())
                    .add(v);
        }
        return map;
    }

    default List<T> toList() {
        List<T> values = new ArrayList<>();
        while (hasNext()) {
            values.add(next());
        }
        return values;
    }

    default Stream<T> stream() {
        Iterable<T> iterable = () -> this;
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
