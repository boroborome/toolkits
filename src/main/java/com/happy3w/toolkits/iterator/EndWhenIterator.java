package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Predicate;

public class EndWhenIterator<T> extends NeedFindIterator<T> {
    private final Iterator<T> innerIterator;
    private final Predicate<T> endPredicate;

    public EndWhenIterator(Iterator<T> innerIterator, Predicate<T> endPredicate) {
        this.innerIterator = innerIterator;
        this.endPredicate = endPredicate;
    }

    @Override
    protected NullableOptional<T> findNext() {
        if (innerIterator.hasNext()) {
            T next = innerIterator.next();
            if (!endPredicate.test(next)) {
                return NullableOptional.of(next);
            }
        }
        return NullableOptional.empty();
    }
}
