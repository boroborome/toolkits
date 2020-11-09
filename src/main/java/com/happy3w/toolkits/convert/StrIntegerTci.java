package com.happy3w.toolkits.convert;

public class StrIntegerTci extends AbstractTci<String, Integer> {
    public StrIntegerTci() {
        super(String.class, Integer.class);
    }

    @Override
    public Integer toTarget(String source) {
        return Integer.parseInt(source);
    }
}
