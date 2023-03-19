package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Consumer;

public class StartActionIterator<T> implements IEasyIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final Consumer<T> startAction;
    protected boolean performed = false;

    public StartActionIterator(Iterator<T> innerIterator, Consumer<T> startAction) {
        this.innerIterator = innerIterator;
        this.startAction = startAction;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public T next() {
        T nextValue = innerIterator.next();
        if (!performed) {
            startAction.accept(nextValue);
            performed = true;
        }
        return nextValue;
    }
}
