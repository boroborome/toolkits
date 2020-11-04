package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;

public class DoubleConverter implements ISimpleConverter<Double, DoubleConverter> {
    @Override
    public Class[] dataTypes() {
        return new Class[] {Double.class, Double.TYPE};
    }

    @Override
    public String convertToString(Double value) {
        return value == null ? null : String.valueOf(value);
    }

    @Override
    public Double convertFromString(String str) {
        return StringUtils.isEmpty(str) ? null : Double.parseDouble(str);
    }
}
