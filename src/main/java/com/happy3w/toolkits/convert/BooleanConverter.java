package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;

public class BooleanConverter implements ISimpleConverter<Boolean, BooleanConverter> {
    @Override
    public Class[] dataTypes() {
        return new Class[] {Boolean.class, Boolean.TYPE};
    }

    @Override
    public String convertToString(Boolean value) {
        return value == null ? null : value.toString();
    }

    @Override
    public Boolean convertFromString(String str) {
        return StringUtils.isEmpty(str) ? null : Boolean.parseBoolean(str);
    }
}
