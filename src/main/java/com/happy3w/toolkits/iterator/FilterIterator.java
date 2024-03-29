package com.happy3w.toolkits.iterator;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.Iterator;
import java.util.function.Predicate;

public class FilterIterator<T, E> extends NeedFindIterator<T> implements IEasyIterator<T> {
    protected final Iterator<E> innerIterator;
    protected final Predicate<E> predicate;


    public FilterIterator(Iterator<E> innerIterator, Predicate<E> predicate) {
        this.innerIterator = innerIterator;
        this.predicate = predicate;
    }

    @Override
    protected NullableOptional<T> findNext() {
        while (innerIterator.hasNext()) {
            E value = innerIterator.next();
            if (predicate.test(value)) {
                return NullableOptional.of((T) value);
            }
        }
        return NullableOptional.empty();
    }
}
