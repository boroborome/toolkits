package com.happy3w.toolkits.iterator;

public interface CloseableIterator<T> extends IEasyIterator<T> {
    void close();
}
