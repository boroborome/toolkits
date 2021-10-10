package com.happy3w.toolkits.convert;

public class BoolIntBiTci extends AbstractBiTci<Boolean, Integer> {
    public BoolIntBiTci() {
        super(Boolean.class, Integer.class);
    }

    @Override
    public Integer toTarget(Boolean source) {
        return Boolean.TRUE.equals(source) ? 1 : 0;
    }

    @Override
    public Boolean toSource(Integer source) {
        return Boolean.valueOf(source != null && source.intValue() != 0);
    }
}
