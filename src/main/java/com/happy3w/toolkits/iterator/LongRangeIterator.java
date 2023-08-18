package com.happy3w.toolkits.iterator;

public class LongRangeIterator implements IEasyIterator<Long> {
    protected long current;
    protected long end;
    protected long step;

    public LongRangeIterator(long start, long end) {
        this(start, end, 1L);
    }

    public LongRangeIterator(long start, long end, long step) {
        this.current = start;
        this.end = end;
        this.step = step;
    }


    @Override
    public boolean hasNext() {
        return step > 0 ? current < end : current > end;
    }

    @Override
    public Long next() {
        long result = current;
        current = current + step;
        return result;
    }
}
