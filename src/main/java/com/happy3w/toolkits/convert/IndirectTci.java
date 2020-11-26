package com.happy3w.toolkits.convert;

import lombok.Getter;

import java.util.List;

public class IndirectTci<S, T> extends AbstractTci<S, T> {
    @Getter
    private List<ITypeConvertItem<?, ?>> convertItems;
    public IndirectTci(Class<S> sourceType, Class<T> targetType, List<ITypeConvertItem<?, ?>> convertItems) {
        super(sourceType, targetType);
        this.convertItems = convertItems;
    }

    @Override
    public T toTarget(S source) {
        Object curValue = source;
        for (ITypeConvertItem tci : convertItems) {
            curValue = tci.toTarget(curValue);
        }
        return (T) curValue;
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (ITypeConvertItem<?, ?> tci : convertItems) {
            Class<?> st = tci.getSourceType();
            buf.append(st == null ? "null" : st.getName())
                    .append(" -> ");
        }
        buf.append(targetType == null ? "null" : targetType.getName());
        return buf.toString();
    }
}
