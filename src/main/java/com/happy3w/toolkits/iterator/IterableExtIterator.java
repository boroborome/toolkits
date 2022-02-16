package com.happy3w.toolkits.iterator;

import java.util.Collections;
import java.util.Iterator;

public class IterableExtIterator<T, E extends T> extends EasyIterator<T> {
    protected final Iterable<E> innerIterable;
    protected Iterator<E> innerIterator;

    public IterableExtIterator(Iterable<E> innerIterable) {
        this.innerIterable = innerIterable == null ? Collections.EMPTY_LIST : innerIterable;
    }

    @Override
    public boolean hasNext() {
        if (innerIterator == null) {
            innerIterator = innerIterable.iterator();
        }
        return innerIterator.hasNext();
    }

    @Override
    public T next() {
        return innerIterator.next();
    }
}
