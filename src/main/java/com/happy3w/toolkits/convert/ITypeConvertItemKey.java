package com.happy3w.toolkits.convert;

public interface ITypeConvertItemKey<S, T> {
    Class<S> getSourceType();
    Class<T> getTargetType();
}
