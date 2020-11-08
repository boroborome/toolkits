package com.happy3w.toolkits.convert;

public class StrLongTci extends AbstractTci<String, Long> {
    public StrLongTci() {
        super(String.class, Long.class);
    }

    @Override
    public Long convert(String source) {
        return Long.parseLong(source);
    }
}
