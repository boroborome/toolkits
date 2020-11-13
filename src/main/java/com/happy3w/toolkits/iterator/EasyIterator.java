package com.happy3w.toolkits.iterator;

import java.util.Arrays;
import java.util.Iterator;
import java.util.stream.Stream;

public abstract class EasyIterator<T> implements IEasyIterator<T> {

    public static <T> EasyIterator<T> from(Stream<T> stream) {
        return new IteratorExtIterator<>(stream.iterator());
    }

    public static <T> EasyIterator<T> from(Iterator<T> iterator) {
        return new IteratorExtIterator<>(iterator);
    }

    public static <T> EasyIterator<T> of(T... values) {
        return new IteratorExtIterator<>(Arrays.asList(values).iterator());
    }

    public static IntRangeIterator range(int start, int end) {
        return new IntRangeIterator(start, end);
    }

}
