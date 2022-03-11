package com.happy3w.toolkits.iterator;

import java.util.Iterator;

public class IteratorExtIterator<T, E extends T> implements IEasyIterator<T> {
    protected final Iterator<E> innerIterator;

    public IteratorExtIterator(Iterator<E> innerIterator) {
        this.innerIterator = innerIterator;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public T next() {
        return innerIterator.next();
    }
}
