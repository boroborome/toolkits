package com.happy3w.toolkits.iterator;

import java.util.Iterator;
import java.util.function.Function;

public class FlatMapIterator<T, R> extends NeedFindIterator<R> {
    protected final Iterator<T> innerIterator;
    protected final Function<T, Iterator<R>> mapMethod;
    private Iterator<R> subInnerIterator;


    public FlatMapIterator(Iterator<T> innerIterator, Function<T, Iterator<R>> mapMethod) {
        this.innerIterator = innerIterator;
        this.mapMethod = mapMethod;
    }

    // CHECKSTYLE:OFF
    @Override
    protected NullableOptional<R> findNext() {
        while ((subInnerIterator == null || !subInnerIterator.hasNext())
                && innerIterator.hasNext()) {
            T innerValue = innerIterator.next();
            subInnerIterator = mapMethod.apply(innerValue);
        }

        if (subInnerIterator != null && subInnerIterator.hasNext()) {
            return NullableOptional.of(subInnerIterator.next());
        }
        return NullableOptional.empty();
    }
    // CHECKSTYLE:ON
}
