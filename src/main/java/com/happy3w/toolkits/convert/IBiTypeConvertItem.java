package com.happy3w.toolkits.convert;

public interface IBiTypeConvertItem<S, T> extends ITypeConvertItemKey<S, T> {
    T convert(S source);
}
