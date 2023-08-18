package com.happy3w.toolkits.iterator;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.function.Function;
import java.util.stream.Stream;

public final class EasyIterator {
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

    public static <T, E extends T> IEasyIterator<T> fromStream(Stream<T> stream) {
        return stream == null
                ? emptyIterator()
                : new IteratorExtIterator<>(stream.iterator())
                .onEnd(stream::close);
    }

    public static <T, E extends T> IEasyIterator<T> fromIterator(Iterator<E> iterator) {
        if (iterator instanceof IEasyIterator) {
            return (IEasyIterator<T>) iterator;
        }
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

    public static LongRangeIterator range(long start, long end) {
        return new LongRangeIterator(start, end);
    }

    public static LongRangeIterator range(long start, long end, long step) {
        return new LongRangeIterator(start, end, step);
    }

    public static BigRangeIterator bigRange(BigInteger start, BigInteger end) {
        return new BigRangeIterator(start, end);
    }

    public static BigRangeIterator bigRange(BigInteger start, BigInteger end, BigInteger step) {
        return new BigRangeIterator(start, end, step);
    }

    public static <T> IEasyIterator<T> emptyIterator() {
        return EMPTY_IT;
    }

    public static <T> IEasyIterator<T> concatAll(Iterator<T>... its) {
        return EasyIterator.of(its)
                .flatMap(Function.identity());
    }
}
