package com.happy3w.toolkits.iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SplitIterator<T> implements IEasyIterator<List<T>> {

    protected final Iterator<T> innerIterator;
    protected final int size;

    public SplitIterator(Iterator<T> innerIterator, int size) {
        this.innerIterator = innerIterator;
        this.size = size;
    }

    @Override
    public boolean hasNext() {
        return innerIterator.hasNext();
    }

    @Override
    public List<T> next() {
        List<T> lst = new ArrayList<>();
        while (innerIterator.hasNext() && lst.size() < size) {
            lst.add(innerIterator.next());
        }
        return lst;
    }
}
