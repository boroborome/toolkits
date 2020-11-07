package com.happy3w.toolkits.utils;

public interface TernaryFunction<I1, I2, I3, R> {
    R apply(I1 i1, I2 i2, I3 i3);
}
