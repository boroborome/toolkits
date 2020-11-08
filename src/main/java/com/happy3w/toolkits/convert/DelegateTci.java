package com.happy3w.toolkits.convert;

import lombok.Getter;

import java.util.function.Function;

public class DelegateTci<S, T> extends AbstractTci<S, T> {
    @Getter
    private Function<S, T> converter;
    public DelegateTci(Class<S> sourceType, Class<T> targetType, Function<S, T> converter) {
        super(sourceType, targetType);
        this.converter = converter;
    }

    @Override
    public T toTarget(S source) {
        return converter.apply(source);
    }
}
