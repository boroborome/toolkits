package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;

public class FilterIterator<T> extends NeedFindIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final Predicate<T> predicate;


    public FilterIterator(Iterator<T> innerIterator, Predicate<T> predicate) {
        this.innerIterator = innerIterator;
        this.predicate = predicate;
    }

    @Override
    protected Optional<T> findNext() {
        while (innerIterator.hasNext()) {
            T value = innerIterator.next();
            if (predicate.test(value)) {
                return Optional.ofNullable(value);
            }
        }
        return Optional.empty();
    }
}
