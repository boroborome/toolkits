package com.happy3w.toolkits.convert;

public interface ITypeConvertItem<S, T> extends ITypeConvertItemKey<S, T> {
    T convert(S source);
}
