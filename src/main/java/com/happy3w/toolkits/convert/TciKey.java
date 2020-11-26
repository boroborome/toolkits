package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.PrimitiveTypeUtil;
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

    @Override
    public String toString() {
        return createDesc(sourceType, targetType);
    }

    public TciKey<S, T> objTypeKey() {
        Class<S> st = PrimitiveTypeUtil.toObjType(sourceType);
        Class<T> tt = PrimitiveTypeUtil.toObjType(targetType);
        if (st != sourceType || tt != targetType) {
            return new TciKey<>(st, tt);
        }
        return this;
    }
}
