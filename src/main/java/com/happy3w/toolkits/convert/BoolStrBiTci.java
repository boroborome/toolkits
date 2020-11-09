package com.happy3w.toolkits.convert;

public class BoolStrBiTci extends AbstractBiTci<Boolean, String> {
    public BoolStrBiTci() {
        super(Boolean.class, String.class);
    }

    @Override
    public String toTarget(Boolean source) {
        return source.toString();
    }

    @Override
    public Boolean toSource(String source) {
        return Boolean.parseBoolean(source);
    }
}
