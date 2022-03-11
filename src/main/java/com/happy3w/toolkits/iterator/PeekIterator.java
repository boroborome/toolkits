package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Consumer;

public class PeekIterator<T> implements IEasyIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final Consumer<T> consumeMethod;

    public PeekIterator(Iterator<T> innerIterator, Consumer<T> consumeMethod) {
        this.innerIterator = innerIterator;
        this.consumeMethod = consumeMethod;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public T next() {
        T v = innerIterator.next();
        consumeMethod.accept(v);
        return v;
    }
}
