package com.happy3w.toolkits.iterator;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface IEasyIterator<T> extends Iterator<T> {

    default IEasyIterator<List<T>> split(int size) {
        return new SplitIterator<>(this, size);
    }

    default <R> IEasyIterator<R> map(Function<T, R> mapMethod) {
        return new MapIterator(this, mapMethod);
    }

    default IEasyIterator<T> peek(Consumer<T> consumeMethod) {
        return new PeekIterator<>(this, consumeMethod);
    }

    default <E> IEasyIterator<E> flatMap(Function<T, Iterator<E>> mapMethod) {
        return new FlatMapIterator<>(this, mapMethod);
    }

    default <E> IEasyIterator<E> flatMapArray(Function<T, E[]> listMethod) {
        return new FlatMapIterator<>(this, t -> {
            E[] array = listMethod.apply(t);
            return array == null ? EasyIterator.emptyIterator() : EasyIterator.of(array);
        });
    }

    default <E> IEasyIterator<E> flatMapList(Function<T, Collection<E>> listMethod) {
        return new FlatMapIterator<>(this, t -> {
            Collection<E> list = listMethod.apply(t);
            return list == null ? EasyIterator.emptyIterator() : list.iterator();
        });
    }
    
    default <E> IEasyIterator<E> flatMapStream(Function<T, Stream<E>> mapMethod) {
        return new FlatMapIterator<>(this, t -> {
            Stream<E> stream = mapMethod.apply(t);
            return stream == null ? EasyIterator.emptyIterator() : stream.iterator();
        });
    }

    default IEasyIterator<T> recurFlatArray(Function<T, T[]> listMethod) {
        return new RecurIterator<>(this, t -> {
            T[] array = listMethod.apply(t);
            return array == null ? EasyIterator.emptyIterator() : EasyIterator.of(array);
        });
    }

    default IEasyIterator<T> recurFlatList(Function<T, Collection<T>> listMethod) {
        return new RecurIterator<>(this, t -> {
            Collection<T> list = listMethod.apply(t);
            return list == null ? EasyIterator.emptyIterator() : list.iterator();
        });
    }

    default IEasyIterator<T> recurFlatStream(Function<T, Stream<T>> mapMethod) {
        return new RecurIterator<>(this, t -> {
            Stream<T> stream = mapMethod.apply(t);
            return stream == null ? null : stream.iterator();
        });
    }

    default IEasyIterator<T> filter(Predicate<T> predicate) {
        return new FilterIterator<T, T>(this, predicate);
    }

    default <E extends T> IEasyIterator<E> filter(Class<E> targetType) {
        return new FilterIterator<E, T>(this, d -> d != null && targetType.isAssignableFrom(d.getClass()));
    }

    default <K> IEasyIterator<Map.Entry<K, List<T>>> groupBy(Function<T, K> keyGenerator) {
        return GroupByIterator.groupBy(this, keyGenerator);
    }

    default <K, V> IEasyIterator<Map.Entry<K, List<V>>> groupBy(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        return GroupByIterator.groupBy(this, keyGenerator, valueGenerator);
    }

    /**
     * 按照Key分组
     * 前一个Iterator必须是已经根据key排序好的
     * @param keyGenerator key的获取方法
     * @param <K> key的类型
     * @return 按照key分组后的Entity迭代器
     */
    default <K> IEasyIterator<Map.Entry<K, List<T>>> aggregateBy(Function<T, K> keyGenerator) {
        return new AggregateListIterator<>(this, keyGenerator, v -> v);
    }

    default <K, V> IEasyIterator<Map.Entry<K, List<V>>> aggregateBy(Function<T, K> keyGenerator, Function<T, V> valueGenerator) {
        return new AggregateListIterator<>(this, keyGenerator, valueGenerator);
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

    default <C> C foldLeftF(Supplier<C> collectorSupplier, BiFunction<C, T, C> action) {
        C c = collectorSupplier.get();
        while (hasNext()) {
            c = action.apply(c, next());
        }
        return c;
    }

    default <C> C foldLeftC(C container, BiConsumer<C, T> action) {
        while (hasNext()) {
            action.accept(container, next());
        }
        return container;
    }

    default <E extends T> IEasyIterator<T> concat(Iterator<E>... its) {
        List<Iterator<? extends T>> newIts = new ArrayList<>();
        newIts.add(this);
        for (Iterator<E> it : its) {
            newIts.add(it);
        }
        newIts.addAll(Arrays.asList(its));
        return EasyIterator.fromIterable(newIts)
                .flatMap(it -> EasyIterator.fromIterator(it).map(d -> d));
    }

    default <E extends T> IEasyIterator<T> concatMix(Object... its) {
        List<Iterator<? extends T>> newIts = new ArrayList<>();
        newIts.add(this);
        for (Object value : its) {
            if (value instanceof Iterator) {
                newIts.add((Iterator<? extends T>) value);
            } else if (value instanceof Iterable) {
                newIts.add(((Iterable<? extends T>) value).iterator());
            } else {
                newIts.add(EasyIterator.of((T) value));
            }
        }
        return EasyIterator.fromIterator(newIts.iterator())
                .flatMap(it -> EasyIterator.fromIterator(it).map(d -> d));
    }

    default IEasyIterator<T> limited(long maxSize) {
        if (maxSize <= 0) {
            return this;
        }
        return new LimitedIterator<>(this, maxSize);
    }

    default IEasyIterator<IndexedItem<T>> indexed() {
        return new IndexedIterator<>(this);
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

    default IEasyIterator<T> sorted() {
        Comparator<T> comp = (Comparator<T>) Comparator.naturalOrder();
        return sorted(comp);
    }

    default IEasyIterator<T> sorted(Comparator<T> comparator) {
        List<T> values = toList();
        values.sort(comparator);
        return EasyIterator.fromIterable(values);
    }

    default Set<T> toSet() {
        Set<T> values = new HashSet<>();
        while (hasNext()) {
            values.add(next());
        }
        return values;
    }

    default Stream<T> stream() {
        Iterable<T> iterable = () -> this;
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    default Optional<T> findFirst() {
        if (hasNext()) {
            return Optional.of(next());
        }
        return Optional.empty();
    }

    default IEasyIterator<T> onStart(Consumer<T> startAction) {
        return new StartActionIterator<>(this, startAction);
    }

    default IEasyIterator<T> onEnd(Runnable endAction) {
        return new EndActionIterator<>(this, endAction);
    }

    default IEasyIterator<T> onItemStart(Consumer<T> startAction) {
        return this.peek(startAction);
    }

    default IEasyIterator<T> onItemEnd(Consumer<T> endAction) {
        return new ItemEndActionIterator<>(this, endAction);
    }

    default IEasyIterator<T> endWhen(Predicate<T> predicate) {
        return new EndWhenIterator<>(this, predicate);
    }

    default <K> IEasyIterator<T> distinct() {
        return distinct(Function.identity());
    }

    default <K> IEasyIterator<T> distinct(Function<T, K> keyGenerator) {
        return new DistinctIterator<>(this, keyGenerator);
    }
}
