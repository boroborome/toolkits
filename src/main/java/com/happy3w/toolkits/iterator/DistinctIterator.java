package com.happy3w.toolkits.iterator;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Function;

public class DistinctIterator<T> extends NeedFindIterator<T> implements IEasyIterator<T> {
    private final Iterator<T> innerIt;
    private final Function<T, ?> keyGenerator;
    private Set<Object> existKeys = new HashSet<>();

    public DistinctIterator(Iterator<T> innerIt, Function<T, ?> keyGenerator) {
        this.innerIt = innerIt;
        this.keyGenerator = keyGenerator;
    }

    @Override
    protected NullableOptional<T> findNext() {
        while (innerIt.hasNext()) {
            T next = innerIt.next();
            Object key = keyGenerator.apply(next);
            if (existKeys.contains(key)) {
                continue;
            }

            existKeys.add(key);
            return NullableOptional.of(next);
        }
        return NullableOptional.empty();
    }
}
