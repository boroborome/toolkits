package com.happy3w.toolkits.convert;

public class NumFloatTci extends AbstractTci<Number, Float> {
    public NumFloatTci() {
        super(Number.class, Float.class);
    }

    @Override
    public Float toTarget(Number source) {
        return source.floatValue();
    }
}
