package com.happy3w.toolkits.convert;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * TypeConvertItemKey<br>
 *     用于表示从源类型到目的类型
 * @param <S> 源类型
 * @param <T> 目的类型
 */
@Getter
@EqualsAndHashCode
public class TciKey<S, T> implements ITypeConvertItemKey<S, T> {
    protected final Class<S> sourceType;
    protected final Class<T> targetType;

    public TciKey(Class<S> sourceType, Class<T> targetType) {
        this.sourceType = sourceType;
        this.targetType = targetType;
    }
}
