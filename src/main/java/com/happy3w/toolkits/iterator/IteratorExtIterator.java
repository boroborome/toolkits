package com.happy3w.toolkits.iterator;

import java.util.Iterator;

public class IteratorExtIterator<T> extends EasyIterator<T> {
    protected final Iterator<T> innerIterator;

    public IteratorExtIterator(Iterator<T> innerIterator) {
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
