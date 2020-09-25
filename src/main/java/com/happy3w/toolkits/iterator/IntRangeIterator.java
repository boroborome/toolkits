package com.happy3w.toolkits.iterator;

public class IntRangeIterator extends NumberRangeIterator<Integer> {
    public IntRangeIterator(int start, int end) {
        super(start, end, 1);
    }

    public IntRangeIterator(int start, int end, int step) {
        super(start, end, step);
    }

    @Override
    protected void increase() {
        current = current + step;
    }
}
