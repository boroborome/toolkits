package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class FilterIterator<T, E> extends NeedFindIterator<T> {
    protected final Iterator<E> innerIterator;
    protected final Predicate<E> predicate;


    public FilterIterator(Iterator<E> innerIterator, Predicate<E> predicate) {
        this.innerIterator = innerIterator;
        this.predicate = predicate;
    }

    @Override
    protected Optional<T> findNext() {
        while (innerIterator.hasNext()) {
            E value = innerIterator.next();
            if (predicate.test(value)) {
                return Optional.ofNullable((T) value);
            }
        }
        return Optional.empty();
    }
}
