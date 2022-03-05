package com.happy3w.toolkits.iterator;

import java.util.Iterator;

public class EndActionIterator<T> implements IEasyIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final Runnable endAction;
    protected boolean performed = false;

    public EndActionIterator(Iterator<T> innerIterator, Runnable endAction) {
        this.innerIterator = innerIterator;
        this.endAction = endAction;
    }

    @Override
    public boolean hasNext() {
        boolean result = innerIterator.hasNext();
        if (!result && !performed) {
            endAction.run();
            performed = true;
        }
        return result;
    }

    @Override
    public T next() {
        return innerIterator.next();
    }
}
