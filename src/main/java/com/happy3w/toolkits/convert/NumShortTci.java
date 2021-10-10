package com.happy3w.toolkits.convert;

public class NumShortTci extends AbstractTci<Number, Short> {
    public NumShortTci() {
        super(Number.class, Short.class);
    }

    @Override
    public Short toTarget(Number source) {
        return source.shortValue();
    }
}
