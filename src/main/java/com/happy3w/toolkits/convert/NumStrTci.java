package com.happy3w.toolkits.convert;

public class NumStrTci extends AbstractTci<Number, String> {
    public NumStrTci() {
        super(Number.class, String.class);
    }

    @Override
    public String convert(Number source) {
        return source.toString();
    }
}
