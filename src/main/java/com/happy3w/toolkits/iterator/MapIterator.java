package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class MapIterator<T, R> extends EasyIterator<R> {
    protected final Iterator<T> innerIterator;
    protected final Function<T, R> mapMethod;

    public MapIterator(Iterator<T> innerIterator, Function<T, R> mapMethod) {
        this.innerIterator = innerIterator;
        this.mapMethod = mapMethod;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public R next() {
        return mapMethod.apply(innerIterator.next());
    }
}
