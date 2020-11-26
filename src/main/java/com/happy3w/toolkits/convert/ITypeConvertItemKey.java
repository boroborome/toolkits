package com.happy3w.toolkits.convert;

public interface ITypeConvertItemKey<S, T> {
    Class<S> getSourceType();
    Class<T> getTargetType();

    default String createDesc(Class<S> sourceType, Class<T> targetType) {
        return (sourceType == null ? "null" : sourceType.getName())
                + " -> "
                + (targetType == null ? "null" : targetType.getName());
    }
}
