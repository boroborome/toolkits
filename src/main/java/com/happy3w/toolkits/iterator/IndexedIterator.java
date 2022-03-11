package com.happy3w.toolkits.iterator;

import java.util.Iterator;

public class IndexedIterator<T> implements IEasyIterator<IndexedItem<T>> {
    protected final Iterator<T> innerIterator;
    private long currentIndex;

    public IndexedIterator(Iterator<T> innerIterator) {
        this.innerIterator = innerIterator;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public IndexedItem<T> next() {
        return new IndexedItem<>(currentIndex++, innerIterator.next());
    }
}
