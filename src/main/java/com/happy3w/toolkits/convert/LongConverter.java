package com.happy3w.toolkits.convert;

import com.happy3w.toolkits.utils.StringUtils;

public class LongConverter implements ISimpleConverter<Long, LongConverter> {
    @Override
    public Class[] dataTypes() {
        return new Class[] {Long.class, Long.TYPE};
    }

    @Override
    public String convertToString(Long value) {
        return value == null ? null : value.toString();
    }

    @Override
    public Long convertFromString(String str) {
        return StringUtils.isEmpty(str) ? null : Long.parseLong(NumConvertUtil.dropPointValue(str));
    }
}
