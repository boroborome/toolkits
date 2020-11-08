package com.happy3w.toolkits.convert;

import lombok.Getter;

@Getter
public abstract class AbstractTci<S, T> extends TciKey<S, T> implements ITypeConvertItem<S, T> {
    public AbstractTci(Class<S> sourceType, Class<T> targetType) {
        super(sourceType, targetType);
    }
}
