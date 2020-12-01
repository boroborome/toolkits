package com.happy3w.toolkits.iterator;

public class ArrayIterator<T> implements IEasyIterator <T>{
    private T[] values;
    private int curIndex = 0;

    public ArrayIterator(T[] values) {
        this.values = values;
    }

    @Override
    public boolean hasNext() {
        return curIndex < values.length;
    }

    @Override
    public T next() {
        if (curIndex < values.length) {
            T v = values[curIndex];
            curIndex++;
            return v;
        }
        return null;
    }
}
