package com.happy3w.toolkits.convert;

public class NumIntegerTci extends AbstractTci<Number, Integer> {
    public NumIntegerTci() {
        super(Number.class, Integer.class);
    }

    @Override
    public Integer toTarget(Number source) {
        return source.intValue();
    }
}
