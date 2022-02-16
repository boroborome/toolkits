package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public abstract class EasyIterator<T> implements IEasyIterator<T> {
    public static final IEasyIterator EMPTY_IT = new IEasyIterator() {
        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    };

    public static <T, E extends T> IEasyIterator<T> fromStream(Stream<E> stream) {
        return stream == null ? emptyIterator() : new IteratorExtIterator<>(stream.iterator());
    }

    public static <T, E extends T> IEasyIterator<T> fromIterator(Iterator<E> iterator) {
        return iterator == null ? emptyIterator() : new IteratorExtIterator<>(iterator);
    }

    public static <T, E extends T> IEasyIterator<T> fromIterable(Iterable<E> iterable) {
        return new IterableExtIterator(iterable);
    }

    public static <T> IEasyIterator<T> of(T... values) {
        if (values == null || values.length == 0) {
            return EMPTY_IT;
        }
        if (values.length == 1) {
            return new SingleIterator<>(values[0]);
        }
        return new ArrayIterator<>(values);
    }

    public static IntRangeIterator range(int start, int end) {
        return new IntRangeIterator(start, end);
    }

    public static <T> IEasyIterator<T> emptyIterator() {
        return EMPTY_IT;
    }

    public static <T> IEasyIterator<T> concatAll(Iterator<T>... its) {
        return EasyIterator.of(its)
                .flatMap(Function.identity());
    }
}
