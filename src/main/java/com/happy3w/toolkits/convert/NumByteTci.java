package com.happy3w.toolkits.convert;

public class NumByteTci extends AbstractTci<Number, Byte> {
    public NumByteTci() {
        super(Number.class, Byte.class);
    }

    @Override
    public Byte toTarget(Number source) {
        return source.byteValue();
    }
}
