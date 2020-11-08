package com.happy3w.toolkits.convert;

public interface IBiTypeConvertItem<S, T> extends ITypeConvertItem<S, T> {
    S toSource(T source);

    default ITypeConvertItem<T,S> reverse() {
        return new DelegateTci<>(getTargetType(), getSourceType(), this::toSource);
    }
}
