package com.happy3w.toolkits.convert;

public class StrFloatTci extends AbstractTci<String, Float> {
    public StrFloatTci() {
        super(String.class, Float.class);
    }

    @Override
    public Float toTarget(String source) {
        return Float.parseFloat(source);
    }
}
