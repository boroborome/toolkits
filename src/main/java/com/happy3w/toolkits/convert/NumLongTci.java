package com.happy3w.toolkits.convert;

public class NumLongTci extends AbstractTci<Number, Long> {
    public NumLongTci() {
        super(Number.class, Long.class);
    }

    @Override
    public Long toTarget(Number source) {
        return source.longValue();
    }
}
