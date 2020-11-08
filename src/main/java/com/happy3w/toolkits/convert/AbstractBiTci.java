package com.happy3w.toolkits.convert;

import lombok.Getter;

@Getter
public abstract class AbstractBiTci<S, T> extends TciKey<S, T> implements IBiTypeConvertItem<S, T> {
    public AbstractBiTci(Class<S> sourceType, Class<T> targetType) {
        super(sourceType, targetType);
    }
}
