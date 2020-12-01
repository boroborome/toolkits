package com.happy3w.toolkits.iterator;

public class SingleIterator<T> implements IEasyIterator <T>{
    private T value;
    private boolean ready = true;

    public SingleIterator(T value) {
        this.value = value;
    }

    @Override
    public boolean hasNext() {
        return ready;
    }

    @Override
    public T next() {
        ready = false;
        return value;
    }
}
