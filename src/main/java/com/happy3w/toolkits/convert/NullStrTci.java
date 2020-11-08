package com.happy3w.toolkits.convert;

public class NullStrTci extends AbstractTci<Void, String> {
    public NullStrTci() {
        super(Void.class, String.class);
    }

    @Override
    public String toTarget(Void source) {
        return "";
    }
}
