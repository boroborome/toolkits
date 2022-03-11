package com.happy3w.toolkits.iterator;

import java.util.Iterator;

public class LimitedIterator<T> implements IEasyIterator<T> {
    protected final Iterator<T> innerIterator;
    protected final long maxSize;
    private long currentIndex;

    /**
     * 限制流最多输出多少条记录
     * @param innerIterator 需要限制的流
     * @param maxSize 最大值，如果最大值小于等于0，则认为不限制
     */
    public LimitedIterator(Iterator<T> innerIterator, long maxSize) {
        this.innerIterator = innerIterator;
        this.maxSize = maxSize;
    }

    @Override
    public boolean hasNext() {
        return (maxSize <= 0 || currentIndex < maxSize) && innerIterator.hasNext();
    }

    @Override
    public T next() {
        currentIndex++;
        return innerIterator.next();
    }
}
