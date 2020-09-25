package com.happy3w.toolkits.iterator;

public abstract class NumberRangeIterator<T extends Number> extends EasyIterator<T> {
    protected T current;
    protected T end;
    protected T step;

    public NumberRangeIterator(T start, T end, T step) {
        this.current = start;
        this.end = end;
        this.step = step;
    }

    @Override
    public boolean hasNext() {
        boolean currentSmall = current.doubleValue() < end.doubleValue();
        return step.doubleValue() > 0 ? currentSmall : !currentSmall;
    }

    @Override
    public T next() {
        T result = current;
        increase();
        return result;
    }

    protected abstract void increase();
}
