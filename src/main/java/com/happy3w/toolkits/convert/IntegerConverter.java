package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;

public class IntegerConverter implements ISimpleConverter<Integer, IntegerConverter> {
    @Override
    public Class[] dataTypes() {
        return new Class[] {Integer.class, Integer.TYPE};
    }

    @Override
    public String convertToString(Integer value) {
        return value == null ? null : value.toString();
    }

    @Override
    public Integer convertFromString(String str) {
        return StringUtils.isEmpty(str) ? null : Integer.parseInt(NumConvertUtil.dropPointValue(str));
    }
}
