package com.happy3w.toolkits.convert;

public class NumDoubleTci extends AbstractTci<Number, Double> {
    public NumDoubleTci() {
        super(Number.class, Double.class);
    }

    @Override
    public Double toTarget(Number source) {
        return source.doubleValue();
    }
}
