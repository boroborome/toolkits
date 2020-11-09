package com.happy3w.toolkits.convert;

public class StrDoubleTci extends AbstractTci<String, Double> {
    public StrDoubleTci() {
        super(String.class, Double.class);
    }

    @Override
    public Double toTarget(String source) {
        return Double.parseDouble(source);
    }
}
