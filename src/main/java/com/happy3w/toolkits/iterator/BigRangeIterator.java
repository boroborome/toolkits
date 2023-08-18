package com.happy3w.toolkits.iterator;

import java.math.BigInteger;

public class BigRangeIterator implements IEasyIterator<BigInteger> {
    protected BigInteger current;
    protected BigInteger end;
    protected BigInteger step;

    public BigRangeIterator(BigInteger start, BigInteger end) {
        this(start, end, BigInteger.ONE);
    }

    public BigRangeIterator(BigInteger start, BigInteger end, BigInteger step) {
        this.current = start;
        this.end = end;
        this.step = step;
    }


    @Override
    public boolean hasNext() {
        int compare = current.compareTo(end);
        return step.signum() > 0 ? compare < 0 : compare > 0;
    }

    @Override
    public BigInteger next() {
        BigInteger result = current;
        current = current.add(step);
        return result;
    }
}
