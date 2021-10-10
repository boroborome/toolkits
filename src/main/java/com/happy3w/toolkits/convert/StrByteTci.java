package com.happy3w.toolkits.convert;

public class StrByteTci extends AbstractTci<String, Byte> {
    public StrByteTci() {
        super(String.class, Byte.class);
    }

    @Override
    public Byte toTarget(String source) {
        return Byte.parseByte(source);
    }
}
