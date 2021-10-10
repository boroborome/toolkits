package com.happy3w.toolkits.convert;

public class StrShortTci extends AbstractTci<String, Short> {
    public StrShortTci() {
        super(String.class, Short.class);
    }

    @Override
    public Short toTarget(String source) {
        return Short.parseShort(source);
    }
}
