package com.happy3w.toolkits.iterator;

import com.happy3w.java.ext.NeedFindIterator;
import com.happy3w.java.ext.NullableOptional;

import java.util.Iterator;
import java.util.Stack;
import java.util.function.Function;

public class RecurIterator<T> extends NeedFindIterator<T> implements IEasyIterator<T> {
    private Stack<Iterator<T>> itStack = new Stack<>();
    private final Function<T, Iterator<T>> recursionMapper;

    public RecurIterator(Iterator<T> innerIterator, Function<T, Iterator<T>> recurMapper) {
        this.recursionMapper = recurMapper;
        itStack.push(innerIterator);
    }

    @Override
    protected NullableOptional<T> findNext() {
        Iterator<T> latest = null;
        while (!itStack.isEmpty()) {
            latest = itStack.peek();
            if (latest.hasNext()) {
                break;
            }
            itStack.pop();
        }

        if (latest == null || !latest.hasNext()) {
            return NullableOptional.empty();
        }

        T nextItem = latest.next();
        Iterator<T> newIt = recursionMapper.apply(nextItem);
        if (newIt != null && newIt.hasNext()) {
            itStack.push(newIt);
        }

        return NullableOptional.of(nextItem);
    }
}
